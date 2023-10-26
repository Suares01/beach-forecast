package com.beachforecast.api.beachforecastapi.clients.openmeteo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;

import com.beachforecast.api.beachforecastapi.clients.Client;
import com.beachforecast.api.beachforecastapi.entities.Beach;
import com.beachforecast.api.beachforecastapi.models.ForecastPoint;

import jakarta.annotation.Resource;
import reactor.core.publisher.Mono;

@Service
public class OpenMeteoClient implements Client {
  private final Logger logger = LogManager.getLogger(OpenMeteoClient.class);

  @Resource(name = "redisTemplate")
  private ValueOperations<String, List<ForecastPoint>> valueOps;

  private final String MARINE_BASE_URL = "https://marine-api.open-meteo.com";
  private final String MARINE_HOURLY = "wave_height,wave_direction,wave_period,swell_wave_height,swell_wave_direction,swell_wave_period";

  private final String WEATHER_BASE_URL = "https://api.open-meteo.com";
  private final String WEATHER_HOURLY = "temperature_2m,visibility,windspeed_10m,winddirection_10m";

  @Override
  public List<ForecastPoint> fetchPoints(Beach beach) {
    logger.info("Fetching points for beach " + beach.getId());

    String cacheKey = generateCacheKey(beach);
    Optional<List<ForecastPoint>> pointsFromCache = getPointsFromCache(cacheKey);

    if (pointsFromCache.isEmpty()) {
      logger.info("Get points from OpenMeteo API for beach " + beach.getId());
      List<ForecastPoint> points = getPointsFromApi(beach.getLat(), beach.getLng());

      cachePoints(cacheKey, points);

      return points;
    }

    logger.info("Get points from cache for beach " + beach.getId());

    return pointsFromCache.get();
  }

  private void cachePoints(String key, List<ForecastPoint> points) {
    valueOps.set(key, points, 60, TimeUnit.MINUTES);
  }

  private String generateCacheKey(Beach beachEntity) {
    return beachEntity.getLat() + "_" + beachEntity.getLng();
  }

  private Optional<List<ForecastPoint>> getPointsFromCache(String key) {
    List<ForecastPoint> points = valueOps.get(key);

    if (points == null) return Optional.empty();

    return Optional.of(points);
  }

  private List<ForecastPoint> getPointsFromApi(float lat, float lng) {
    var datePeriod = calculateDatePeriod();
    var marineClientArgs = new WebClientArgs(MARINE_BASE_URL, "/v1/marine", MARINE_HOURLY, "America/Sao_Paulo", lat, lng, datePeriod);
    var weatherClientArgs = new WebClientArgs(WEATHER_BASE_URL, "/v1/gfs", WEATHER_HOURLY, "America/Sao_Paulo", lat, lng, datePeriod);

    Mono<MarineResponse> monoMarineResponse = buildWebClient(marineClientArgs)
        .retrieve()
        .bodyToMono(MarineResponse.class);

    Mono<WeatherResponse> monoWeatherResponse = buildWebClient(weatherClientArgs)
        .retrieve()
        .bodyToMono(WeatherResponse.class);

    return Mono.zip(monoMarineResponse, monoWeatherResponse)
        .map(tuple -> normalizeData(tuple.getT1(), tuple.getT2()))
        .block();
  }

  private List<ForecastPoint> normalizeData(MarineResponse marineResponse, WeatherResponse weatherResponse) {
    MarineHourly marineHourly = marineResponse.getHourly();
    WeatherHourly weatherHourly = weatherResponse.getHourly();

    int size = marineHourly.getTime().length;
    return IntStream.range(0, size)
        .mapToObj(i -> {
          String time = marineHourly.getTime()[i];
          int swellDirection = marineHourly.getSwell_wave_direction()[i];
          float swellHeight = marineHourly.getSwell_wave_height()[i];
          float swellPeriod = marineHourly.getSwell_wave_period()[i];
          int waveDirection = marineHourly.getWave_direction()[i];
          float waveHeight = marineHourly.getWave_height()[i];
          float wavePeriod = marineHourly.getWave_period()[i];
          int windDirection = weatherHourly.getWinddirection_10m()[i];
          float windSpeed = weatherHourly.getWindspeed_10m()[i];
          float visibility = weatherHourly.getVisibility()[i];
          float temperature = weatherHourly.getTemperature_2m()[i];

          return new ForecastPoint(
              time,
              swellDirection,
              swellHeight,
              swellPeriod,
              waveDirection,
              waveHeight,
              wavePeriod,
              windDirection,
              windSpeed,
              visibility,
              temperature);
        })
        .collect(Collectors.toList());
  }

  private DatePeriod calculateDatePeriod() {
    LocalDate startDate = LocalDate.now();
    LocalDate endDate = startDate.plusDays(4);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formattedStartDate = startDate.format(formatter);
    String formattedEndDate = endDate.format(formatter);

    return new DatePeriod(formattedStartDate, formattedEndDate);
  }

  private RequestBodySpec buildWebClient(WebClientArgs args) {
    return WebClient
        .builder()
        .baseUrl(args.baseUrl)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build()
        .method(HttpMethod.GET)
        .uri((uriBuilder) -> uriBuilder
            .path(args.path)
            .queryParam("latitude", args.lat)
            .queryParam("longitude", args.lng)
            .queryParam("hourly", args.hourly)
            .queryParam("timezone", args.timezone)
            .queryParam("start_date", args.datePeriod.startDate)
            .queryParam("end_date", args.datePeriod.endDate)
            .build())
        .accept(MediaType.APPLICATION_JSON);
  }

  private record DatePeriod(String startDate, String endDate) {
  }

  private record WebClientArgs(String baseUrl, String path, String hourly, String timezone, float lat, float lng, DatePeriod datePeriod) {
  }
}
