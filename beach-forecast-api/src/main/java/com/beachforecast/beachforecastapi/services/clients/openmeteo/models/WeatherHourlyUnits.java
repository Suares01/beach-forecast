package com.beachforecast.beachforecastapi.services.clients.openmeteo.models;

public class WeatherHourlyUnits {
  private String time;
  private String temperature_2m;
  private String visibility;
  private String windspeed_10m;
  private String winddirection_10m;

  public String getTime() {
    return this.time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getTemperature_2m() {
    return this.temperature_2m;
  }

  public void setTemperature_2m(String temperature_2m) {
    this.temperature_2m = temperature_2m;
  }

  public String getVisibility() {
    return this.visibility;
  }

  public void setVisibility(String visibility) {
    this.visibility = visibility;
  }

  public String getWindspeed_10m() {
    return this.windspeed_10m;
  }

  public void setWindspeed_10m(String windspeed_10m) {
    this.windspeed_10m = windspeed_10m;
  }

  public String getWinddirection_10m() {
    return this.winddirection_10m;
  }

  public void setWinddirection_10m(String winddirection_10m) {
    this.winddirection_10m = winddirection_10m;
  }
}
