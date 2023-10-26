package com.beachforecast.api.beachforecastapi.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TimeForecast {
  private String time;
  private List<BeachForecast> forecasts;
}
