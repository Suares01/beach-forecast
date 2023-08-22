package com.beachforecast.beachforecastapi.services.clients.openmeteo.models;

public class MarineHourlyUnits {
  private String time;
  private String wave_height;
  private String wave_direction;
  private String wave_period;
  private String swell_wave_height;
  private String swell_wave_direction;
  private String swell_wave_period;

  public String getTime() {
    return this.time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getWave_height() {
    return this.wave_height;
  }

  public void setWave_height(String wave_height) {
    this.wave_height = wave_height;
  }

  public String getWave_direction() {
    return this.wave_direction;
  }

  public void setWave_direction(String wave_direction) {
    this.wave_direction = wave_direction;
  }

  public String getWave_period() {
    return this.wave_period;
  }

  public void setWave_period(String wave_period) {
    this.wave_period = wave_period;
  }

  public String getSwell_wave_height() {
    return this.swell_wave_height;
  }

  public void setSwell_wave_height(String swell_wave_height) {
    this.swell_wave_height = swell_wave_height;
  }

  public String getSwell_wave_direction() {
    return this.swell_wave_direction;
  }

  public void setSwell_wave_direction(String swell_wave_direction) {
    this.swell_wave_direction = swell_wave_direction;
  }

  public String getSwell_wave_period() {
    return this.swell_wave_period;
  }

  public void setSwell_wave_period(String swell_wave_period) {
    this.swell_wave_period = swell_wave_period;
  }
}
