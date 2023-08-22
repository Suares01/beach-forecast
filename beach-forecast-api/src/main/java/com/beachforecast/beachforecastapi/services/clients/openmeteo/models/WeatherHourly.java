package com.beachforecast.beachforecastapi.services.clients.openmeteo.models;

import java.util.List;

public class WeatherHourly {
  private List<String> time;
  private List<Double> temperature_2m;
  private List<Double> visibility;
  private List<Double> windspeed_10m;
  private List<Integer> winddirection_10m;

  public List<String> getTime() {
    return this.time;
  }

  public void setTime(List<String> time) {
    this.time = time;
  }

  public List<Double> getTemperature_2m() {
    return this.temperature_2m;
  }

  public void setTemperature_2m(List<Double> temperature_2m) {
    this.temperature_2m = temperature_2m;
  }

  public List<Double> getVisibility() {
    return this.visibility;
  }

  public void setVisibility(List<Double> visibility) {
    this.visibility = visibility;
  }

  public List<Double> getWindspeed_10m() {
    return this.windspeed_10m;
  }

  public void setWindspeed_10m(List<Double> windspeed_10m) {
    this.windspeed_10m = windspeed_10m;
  }

  public List<Integer> getWinddirection_10m() {
    return this.winddirection_10m;
  }

  public void setWinddirection_10m(List<Integer> winddirection_10m) {
    this.winddirection_10m = winddirection_10m;
  }

}
