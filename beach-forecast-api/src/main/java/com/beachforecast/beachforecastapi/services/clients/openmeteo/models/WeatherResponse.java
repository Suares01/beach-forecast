package com.beachforecast.beachforecastapi.services.clients.openmeteo.models;

public class WeatherResponse {
  private Double latitude;
  private Double longitude;
  private Double generationtime_ms;
  private Integer utc_offset_seconds;
  private String timezone;
  private String timezone_abbreviation;
  private Double elevation;
  private WeatherHourlyUnits hourly_units;
  private WeatherHourly hourly;

  public Double getLatitude() {
    return this.latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return this.longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public Double getGenerationtime_ms() {
    return this.generationtime_ms;
  }

  public void setGenerationtime_ms(Double generationtime_ms) {
    this.generationtime_ms = generationtime_ms;
  }

  public Integer getUtc_offset_seconds() {
    return this.utc_offset_seconds;
  }

  public void setUtc_offset_seconds(Integer utc_offset_seconds) {
    this.utc_offset_seconds = utc_offset_seconds;
  }

  public String getTimezone() {
    return this.timezone;
  }

  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

  public String getTimezone_abbreviation() {
    return this.timezone_abbreviation;
  }

  public void setTimezone_abbreviation(String timezone_abbreviation) {
    this.timezone_abbreviation = timezone_abbreviation;
  }

  public Double getElevation() {
    return this.elevation;
  }

  public void setElevation(Double elevation) {
    this.elevation = elevation;
  }

  public WeatherHourlyUnits getHourly_units() {
    return this.hourly_units;
  }

  public void setHourly_units(WeatherHourlyUnits hourly_units) {
    this.hourly_units = hourly_units;
  }

  public WeatherHourly getHourly() {
    return this.hourly;
  }

  public void setHourly(WeatherHourly hourly) {
    this.hourly = hourly;
  }
}
