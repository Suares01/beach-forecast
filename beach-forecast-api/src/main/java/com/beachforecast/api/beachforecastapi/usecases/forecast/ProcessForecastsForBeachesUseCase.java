package com.beachforecast.api.beachforecastapi.usecases.forecast;

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
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beachforecast.api.beachforecastapi.clients.openmeteo.OpenMeteoClient;
import com.beachforecast.api.beachforecastapi.entities.Beach;
import com.beachforecast.api.beachforecastapi.entities.Rating;
import com.beachforecast.api.beachforecastapi.models.BeachForecast;
import com.beachforecast.api.beachforecastapi.models.ForecastPoint;
import com.beachforecast.api.beachforecastapi.models.ForecastRating;
import com.beachforecast.api.beachforecastapi.models.TimeForecast;
import com.beachforecast.api.beachforecastapi.repositories.BeachRepository;
import com.beachforecast.api.beachforecastapi.repositories.RatingRepository;
import com.beachforecast.api.beachforecastapi.services.ForecastRatingService;

@Service
public class ProcessForecastsForBeachesUseCase {
  private final Logger logger = LogManager.getLogger(ProcessForecastsForBeachesUseCase.class);

  @Autowired
  private BeachRepository beachRepository;

  @Autowired
  private RatingRepository ratingRepository;

  @Autowired
  private OpenMeteoClient client;

  @Autowired
  private ForecastRatingService forecastRatingService;

  public List<TimeForecast> execute(UUID userId) throws InterruptedException, ExecutionException {
    final List<Beach> beaches = getUserBeaches(userId);

    if (beaches.size() == 0) {
      return List.of();
    }

    logger.info("Processing forecasts for " + beaches.size() + " beaches.");

    final List<Rating> ratings = getUserRatings(userId);
    final List<List<ForecastPoint>> beachesPoints = fetchBeachesPoints(beaches);
    final List<BeachForecast> forecasts = processForecasts(beaches, ratings, beachesPoints);
    final List<TimeForecast> timeForecasts = mapForecastByTime(forecasts);

    return timeForecasts;
  }

  private List<Beach> getUserBeaches(UUID userId) {
    return beachRepository.findByUserId(userId);
  }

  private List<Rating> getUserRatings(UUID userId) {
    return ratingRepository.findByUserId(userId);
  }

  private List<List<ForecastPoint>> fetchBeachesPoints(List<Beach> beaches) throws InterruptedException, ExecutionException {
    int availableProcessors = Runtime.getRuntime().availableProcessors();
    int beachesSize = beaches.size();
    int numThreads = availableProcessors > beachesSize ? beachesSize : availableProcessors;
    final ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

    List<Callable<List<ForecastPoint>>> tasks = beaches.stream()
        .map(beach -> (Callable<List<ForecastPoint>>) () -> client.fetchPoints(beach))
        .collect(Collectors.toList());

    List<Future<List<ForecastPoint>>> futures = executorService.invokeAll(tasks);
    List<List<ForecastPoint>> points = futures.stream()
        .map(future -> {
          try {
            return future.get();
          } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e.getMessage());
          }
        })
        .collect(Collectors.toList());

    executorService.shutdown();
    executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

    return points;
  }

  private List<BeachForecast> processForecasts(List<Beach> beaches, List<Rating> ratings, List<List<ForecastPoint>> beachesPoints) {
    return beachesPoints.stream()
        .flatMap(points -> {
          Beach beach = beaches.get(beachesPoints.indexOf(points));

          return points.stream()
              .map(point -> {
                List<ForecastRating> ratingsList = calculateRatingsForPoint(ratings, point, beach);

                return new BeachForecast(point, beach, ratingsList);
              });
        })
        .collect(Collectors.toList());
  }

  private List<ForecastRating> calculateRatingsForPoint(List<Rating> ratings, ForecastPoint point, Beach beach) {
    return ratings.stream()
        .map(rating -> {
          int value = forecastRatingService.evaluateConditions(point, beach, rating);
          return new ForecastRating(rating.getId(), rating.getName(), rating.getType(), rating.getCriteria(), value);
        })
        .collect(Collectors.toList());
  }

  private List<TimeForecast> mapForecastByTime(List<BeachForecast> beachForecasts) {
    Map<String, List<BeachForecast>> forecastMap = beachForecasts.stream()
        .collect(Collectors.groupingBy(BeachForecast::getTime));

    TreeMap<String, List<BeachForecast>> sortedForecastMap = new TreeMap<>(forecastMap);

    return sortedForecastMap.entrySet().stream()
        .map(entry -> new TimeForecast(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }
}
