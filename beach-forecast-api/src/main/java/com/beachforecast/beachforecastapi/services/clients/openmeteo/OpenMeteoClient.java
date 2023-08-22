package com.beachforecast.beachforecastapi.services.clients.openmeteo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;

import com.beachforecast.beachforecastapi.entities.BeachEntity;
import com.beachforecast.beachforecastapi.services.clients.Client;
import com.beachforecast.beachforecastapi.services.clients.openmeteo.models.MarineHourly;
import com.beachforecast.beachforecastapi.services.clients.openmeteo.models.MarineResponse;
import com.beachforecast.beachforecastapi.services.clients.openmeteo.models.WeatherHourly;
import com.beachforecast.beachforecastapi.services.clients.openmeteo.models.WeatherResponse;

import jakarta.annotation.Resource;
import reactor.core.publisher.Mono;

@Service
public class OpenMeteoClient implements Client {
  @Resource(name = "redisTemplate")
  private ValueOperations<String, List<ForecastPoint>> valueOps;

  private final String MARINE_BASE_URL = "https://marine-api.open-meteo.com";
  private final String MARINE_HOURLY = "wave_height,wave_direction,wave_period,swell_wave_height,swell_wave_direction,swell_wave_period";

  private final String WEATHER_BASE_URL = "https://api.open-meteo.com";
  private final String WEATHER_HOURLY = "temperature_2m,visibility,windspeed_10m,winddirection_10m";

  private final String TIMEZONE = "America/Sao_Paulo";

  private record DatePeriod(String startDate, String endDate) {
  }

  public List<ForecastPoint> fetchPoints(final BeachEntity beach) {
    String cacheKey = generateCacheKey(beach);
    List<ForecastPoint> pointsFromCache = valueOps.get(cacheKey);

    if (pointsFromCache == null) {
      List<ForecastPoint> points = getPointsFromApi(beach.getLat(), beach.getLng());

      valueOps.set(cacheKey, points, 60, TimeUnit.MINUTES);

      return points;
    }

    return pointsFromCache;
  }

  private String generateCacheKey(BeachEntity beachEntity) {
    return beachEntity.getLat() + "_" + beachEntity.getLng();
  }

  private List<ForecastPoint> getPointsFromApi(final Double lat, final Double lng) {
    var datePeriod = calculateDatePeriod();

    Mono<MarineResponse> monoMarineResponse = buildMarineClient(lat, lng, datePeriod)
        .retrieve()
        .bodyToMono(MarineResponse.class);

    Mono<WeatherResponse> monoWeatherResponse = buildWeatherClient(lat, lng, datePeriod)
        .retrieve()
        .bodyToMono(WeatherResponse.class);

    MarineResponse marineResponse = monoMarineResponse.block();
    WeatherResponse weatherResponse = monoWeatherResponse.block();

    return normalizeData(marineResponse, weatherResponse);
  }

  private List<ForecastPoint> normalizeData(MarineResponse marineResponse, WeatherResponse weatherResponse) {
    List<ForecastPoint> points = new ArrayList<>();

    for (Integer i = 0; i < marineResponse.getHourly().getTime().size(); i++) {
      MarineHourly marineHourly = marineResponse.getHourly();
      WeatherHourly weatherHourly = weatherResponse.getHourly();

      String time = marineHourly.getTime().get(i);
      Integer swellDirection = marineHourly.getSwell_wave_direction().get(i);
      Double swellHeight = marineHourly.getSwell_wave_height().get(i);
      Double swellPeriod = marineHourly.getSwell_wave_period().get(i);
      Integer waveDirection = marineHourly.getWave_direction().get(i);
      Double waveHeight = marineHourly.getSwell_wave_height().get(i);
      Double wavePeriod = marineHourly.getSwell_wave_period().get(i);
      Integer windDirection = weatherHourly.getWinddirection_10m().get(i);
      Double windSpeed = weatherHourly.getWindspeed_10m().get(i);
      Double visibility = weatherHourly.getVisibility().get(i);
      Double temperature = weatherHourly.getTemperature_2m().get(i);

      var forecastPoInteger = new ForecastPoint(
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

      points.add(forecastPoInteger);
    }

    return points;
  }

  private DatePeriod calculateDatePeriod() {
    LocalDate startDate = LocalDate.now();
    LocalDate endDate = startDate.plusDays(6);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formattedStartDate = startDate.format(formatter);
    String formattedEndDate = endDate.format(formatter);

    return new DatePeriod(formattedStartDate, formattedEndDate);
  }

  private RequestBodySpec buildMarineClient(Double lat, Double lng, DatePeriod datePeriod) {
    return WebClient
        .builder()
        .baseUrl(MARINE_BASE_URL)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build()
        .method(HttpMethod.GET)
        .uri((uriBuilder) -> uriBuilder
            .path("/v1/marine")
            .queryParam("latitude", lat)
            .queryParam("longitude", lng)
            .queryParam("hourly", MARINE_HOURLY)
            .queryParam("timezone", TIMEZONE)
            .queryParam("start_date", datePeriod.startDate())
            .queryParam("end_date", datePeriod.endDate())
            .build());
  }

  private RequestBodySpec buildWeatherClient(Double lat, Double lng, DatePeriod datePeriod) {
    return WebClient
        .builder()
        .baseUrl(WEATHER_BASE_URL)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build()
        .method(HttpMethod.GET)
        .uri((uriBuilder) -> uriBuilder
            .path("/v1/gfs")
            .queryParam("latitude", lat)
            .queryParam("longitude", lng)
            .queryParam("hourly", WEATHER_HOURLY)
            .queryParam("timezone", TIMEZONE)
            .queryParam("start_date", datePeriod.startDate())
            .queryParam("end_date", datePeriod.endDate())
            .build());
  }
}
