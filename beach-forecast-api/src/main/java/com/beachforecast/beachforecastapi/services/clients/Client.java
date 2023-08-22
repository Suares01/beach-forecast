package com.beachforecast.beachforecastapi.services.clients;

import java.io.Serializable;
import java.util.List;

import com.beachforecast.beachforecastapi.entities.BeachEntity;

public interface Client {

  public class ForecastPoint implements Serializable {
    private String time;
    private Integer swellDirection;
    private Double swellHeight;
    private Double swellPeriod;
    private Integer waveDirection;
    private Double waveHeight;
    private Double wavePeriod;
    private Integer windDirection;
    private Double windSpeed;
    private Double visibility;
    private Double temperature;

    public ForecastPoint(String time, Integer swellDirection, Double swellHeight, Double swellPeriod,
        Integer waveDirection, Double waveHeight, Double wavePeriod, Integer windDirection, Double windSpeed,
        Double visibility, Double temperature) {
      this.time = time;
      this.swellDirection = swellDirection;
      this.swellHeight = swellHeight;
      this.swellPeriod = swellPeriod;
      this.waveDirection = waveDirection;
      this.waveHeight = waveHeight;
      this.wavePeriod = wavePeriod;
      this.windDirection = windDirection;
      this.windSpeed = windSpeed;
      this.visibility = visibility;
      this.temperature = temperature;
    }

    public String getTime() {
      return this.time;
    }

    public void setTime(String time) {
      this.time = time;
    }

    public Integer getSwellDirection() {
      return this.swellDirection;
    }

    public void setSwellDirection(Integer swellDirection) {
      this.swellDirection = swellDirection;
    }

    public Double getSwellHeight() {
      return this.swellHeight;
    }

    public void setSwellHeight(Double swellHeight) {
      this.swellHeight = swellHeight;
    }

    public Double getSwellPeriod() {
      return this.swellPeriod;
    }

    public void setSwellPeriod(Double swellPeriod) {
      this.swellPeriod = swellPeriod;
    }

    public Integer getWaveDirection() {
      return this.waveDirection;
    }

    public void setWaveDirection(Integer waveDirection) {
      this.waveDirection = waveDirection;
    }

    public Double getWaveHeight() {
      return this.waveHeight;
    }

    public void setWaveHeight(Double waveHeight) {
      this.waveHeight = waveHeight;
    }

    public Double getWavePeriod() {
      return this.wavePeriod;
    }

    public void setWavePeriod(Double wavePeriod) {
      this.wavePeriod = wavePeriod;
    }

    public Integer getWindDirection() {
      return this.windDirection;
    }

    public void setWindDirection(Integer windDirection) {
      this.windDirection = windDirection;
    }

    public Double getWindSpeed() {
      return this.windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
      this.windSpeed = windSpeed;
    }

    public Double getVisibility() {
      return this.visibility;
    }

    public void setVisibility(Double visibility) {
      this.visibility = visibility;
    }

    public Double getTemperature() {
      return this.temperature;
    }

    public void setTemperature(Double temperature) {
      this.temperature = temperature;
    }
  }

  public List<ForecastPoint> fetchPoints(BeachEntity beach);
}
