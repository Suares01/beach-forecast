package com.beachforecast.beachforecastapi.useCases.forecasts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beachforecast.beachforecastapi.entities.BeachEntity;
import com.beachforecast.beachforecastapi.entities.CriterionEntity;
import com.beachforecast.beachforecastapi.entities.RatingEntity;
import com.beachforecast.beachforecastapi.entities.RatingEntity.RatingType;
import com.beachforecast.beachforecastapi.entities.UserEntity;
import com.beachforecast.beachforecastapi.repositories.BeachRepository;
import com.beachforecast.beachforecastapi.repositories.RatingRepository;
import com.beachforecast.beachforecastapi.services.ForecastRatingService;
import com.beachforecast.beachforecastapi.services.clients.Client.ForecastPoint;
import com.beachforecast.beachforecastapi.services.clients.openmeteo.OpenMeteoClient;

@Service
public class ProcessForecastsForBeachesUseCase {
  @Autowired
  private BeachRepository beachRepository;

  @Autowired
  private RatingRepository ratingRepository;

  @Autowired
  private OpenMeteoClient client;

  @Autowired
  private ForecastRatingService forecastRatingService;

  public class BeachRating {
    private UUID id;
    private String name;
    private RatingType type;
    private List<CriterionEntity> criteria = new ArrayList<>();
    private Long value;

    public BeachRating(UUID id, String name, RatingType type, List<CriterionEntity> criteria, Long value) {
      this.id = id;
      this.name = name;
      this.type = type;
      this.criteria = criteria;
      this.value = value;
    }

    public UUID getId() {
      return id;
    }

    public void setId(UUID id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public RatingType getType() {
      return type;
    }

    public void setType(RatingType type) {
      this.type = type;
    }

    public List<CriterionEntity> getCriteria() {
      return criteria;
    }

    public void setCriteria(List<CriterionEntity> criteria) {
      this.criteria = criteria;
    }

    public Long getValue() {
      return value;
    }

    public void setValue(Long value) {
      this.value = value;
    }
  }

  public class BeachForecast extends ForecastPoint {
    private BeachEntity beach;
    private List<BeachRating> ratings;

    public BeachForecast(String time, Integer swellDirection, Double swellHeight, Double swellPeriod,
        Integer waveDirection, Double waveHeight, Double wavePeriod, Integer windDirection, Double windSpeed,
        Double visibility, Double temperature, BeachEntity beach,
        List<BeachRating> ratings) {
      super(time, swellDirection, swellHeight, swellPeriod, waveDirection, waveHeight, wavePeriod, windDirection,
          windSpeed, visibility, temperature);
      this.beach = beach;
      this.ratings = ratings;
    }

    public BeachEntity getBeach() {
      return beach;
    }

    public void setBeach(BeachEntity beach) {
      this.beach = beach;
    }

    public List<BeachRating> getRatings() {
      return ratings;
    }

    public void setRatings(List<BeachRating> ratings) {
      this.ratings = ratings;
    }
  }

  public class TimeForecast {
    private String time;
    private List<BeachForecast> forecasts;

    public TimeForecast(String time, List<BeachForecast> forecasts) {
      this.time = time;
      this.forecasts = forecasts;
    }

    public String getTime() {
      return time;
    }

    public void setTime(String time) {
      this.time = time;
    }

    public List<BeachForecast> getForecasts() {
      return forecasts;
    }

    public void setForecasts(List<BeachForecast> forecasts) {
      this.forecasts = forecasts;
    }
  }

  public List<TimeForecast> execute(UserEntity user) throws InterruptedException, ExecutionException {
    final List<BeachEntity> beaches = beachRepository.findByUser(user);

    if (beaches.size() == 0) {
      return new ArrayList<TimeForecast>();
    }

    final List<RatingEntity> ratings = ratingRepository.findByUser(user);

    final List<List<ForecastPoint>> beachesPoints = fetchBeachesPoints(beaches);
    final List<BeachForecast> forecasts = processForecasts(beaches, ratings, beachesPoints);
    final List<TimeForecast> timeForecasts = mapForecastByTime(forecasts);

    return timeForecasts;
  }

  private List<List<ForecastPoint>> fetchBeachesPoints(List<BeachEntity> beaches) throws InterruptedException, ExecutionException {
    int numThreads = Runtime.getRuntime().availableProcessors();
    final ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

    List<Callable<List<ForecastPoint>>> tasks = new ArrayList<>();

    for (BeachEntity beach : beaches) {
      tasks.add(() -> client.fetchPoints(beach));
    }

    List<Future<List<ForecastPoint>>> futures = executorService.invokeAll(tasks);
    List<List<ForecastPoint>> points = new ArrayList<>();

    for (Future<List<ForecastPoint>> future : futures) {
      points.add(future.get());
    }

    executorService.shutdown();
    executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

    return points;
  }

  private List<BeachForecast> processForecasts(List<BeachEntity> beaches, List<RatingEntity> ratings,
      List<List<ForecastPoint>> beachesPoints) {
    List<BeachForecast> forecasts = new ArrayList<>();

    for (Integer i = 0; i < beachesPoints.size(); i++) {
      List<ForecastPoint> points = beachesPoints.get(i);
      BeachEntity beach = beaches.get(i);

      for (ForecastPoint point : points) {
        List<BeachRating> ratingsList = new ArrayList<>();

        for (RatingEntity rating : ratings) {
          Long value = forecastRatingService.evaluateConditions(point, beach, rating);

          var beachRating = new BeachRating(rating.getId(), rating.getName(), rating.getType(), rating.getCriteria(),
              value);
          ratingsList.add(beachRating);
        }

        var beachForecast = new BeachForecast(point.getTime(), point.getSwellDirection(), point.getSwellHeight(),
            point.getSwellPeriod(), point.getWaveDirection(), point.getWaveHeight(), point.getWavePeriod(),
            point.getWindDirection(), point.getWindSpeed(), point.getVisibility(), point.getTemperature(),
            beach, ratingsList);

        forecasts.add(beachForecast);
      }
    }

    return forecasts;
  }

  public List<TimeForecast> mapForecastByTime(List<BeachForecast> beachForecasts) {
    Map<String, List<BeachForecast>> forecastMap = new HashMap<>();

    for (BeachForecast forecast : beachForecasts) {
      String time = forecast.getTime();
      forecastMap.computeIfAbsent(time, k -> new ArrayList<>()).add(forecast);
    }

    TreeMap<String, List<BeachForecast>> sortedForecastMap = new TreeMap<>(forecastMap);

    List<TimeForecast> timeForecasts = new ArrayList<>();

    for (Map.Entry<String, List<BeachForecast>> entry : sortedForecastMap.entrySet()) {
      String time = entry.getKey();
      List<BeachForecast> forecasts = entry.getValue();
      timeForecasts.add(new TimeForecast(time, forecasts));
    }

    return timeForecasts;
  }
}
