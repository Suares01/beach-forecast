package com.beachforecast.api.beachforecastapi.clients.openmeteo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarineHourly {
  private String[] time = new String[120];
  private float[] wave_height = new float[120];
  private int[] wave_direction = new int[120];
  private float[] wave_period = new float[120];
  private float[] swell_wave_height = new float[120];
  private int[] swell_wave_direction = new int[120];
  private float[] swell_wave_period = new float[120];
}
