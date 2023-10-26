package com.beachforecast.api.beachforecastapi.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ForecastPoint implements Serializable {
  private String time;
  private int swellDirection;
  private float swellHeight;
  private float swellPeriod;
  private int waveDirection;
  private float waveHeight;
  private float wavePeriod;
  private int windDirection;
  private float windSpeed;
  private float visibility;
  private float temperature;
}
