package com.beachforecast.api.beachforecastapi.clients.openmeteo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherHourly {
  private String[] time = new String[120];
  private float[] temperature_2m = new float[120];
  private float[] visibility = new float[120];
  private float[] windspeed_10m = new float[120];
  private int[] winddirection_10m = new int[120];
}
