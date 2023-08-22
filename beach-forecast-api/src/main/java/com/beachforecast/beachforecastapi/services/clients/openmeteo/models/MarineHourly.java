package com.beachforecast.beachforecastapi.services.clients.openmeteo.models;

import java.util.List;

public class MarineHourly {
  private List<String> time;
  private List<Double> wave_height;
  private List<Integer> wave_direction;
  private List<Double> wave_period;
  private List<Double> swell_wave_height;
  private List<Integer> swell_wave_direction;
  private List<Double> swell_wave_period;

  public List<String> getTime() {
    return this.time;
  }

  public void setTime(List<String> time) {
    this.time = time;
  }

  public List<Double> getWave_height() {
    return this.wave_height;
  }

  public void setWave_height(List<Double> wave_height) {
    this.wave_height = wave_height;
  }

  public List<Integer> getWave_direction() {
    return this.wave_direction;
  }

  public void setWave_direction(List<Integer> wave_direction) {
    this.wave_direction = wave_direction;
  }

  public List<Double> getWave_period() {
    return this.wave_period;
  }

  public void setWave_period(List<Double> wave_period) {
    this.wave_period = wave_period;
  }

  public List<Double> getSwell_wave_height() {
    return this.swell_wave_height;
  }

  public void setSwell_wave_height(List<Double> swell_wave_height) {
    this.swell_wave_height = swell_wave_height;
  }

  public List<Integer> getSwell_wave_direction() {
    return this.swell_wave_direction;
  }

  public void setSwell_wave_direction(List<Integer> swell_wave_direction) {
    this.swell_wave_direction = swell_wave_direction;
  }

  public List<Double> getSwell_wave_period() {
    return this.swell_wave_period;
  }

  public void setSwell_wave_period(List<Double> swell_wave_period) {
    this.swell_wave_period = swell_wave_period;
  }
}
