package com.beachforecast.api.beachforecastapi.clients.openmeteo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherResponse {
  private WeatherHourly hourly;
}
