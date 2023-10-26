package com.beachforecast.api.beachforecastapi.models;

import java.util.List;

import com.beachforecast.api.beachforecastapi.entities.Beach;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BeachForecast extends ForecastPoint {
  private Beach beach;
  private List<ForecastRating> ratings;

  public BeachForecast(ForecastPoint point, Beach beach, List<ForecastRating> ratings) {
    super(point.getTime(), point.getSwellDirection(), point.getWaveHeight(), point.getSwellPeriod(),
        point.getWaveDirection(), point.getWaveHeight(), point.getWavePeriod(), point.getWindDirection(),
        point.getWindSpeed(), point.getVisibility(), point.getTemperature());
    this.beach = beach;
    this.ratings = ratings;
  }
}
