package com.beachforecast.api.beachforecastapi.clients;

import java.util.List;

import com.beachforecast.api.beachforecastapi.entities.Beach;
import com.beachforecast.api.beachforecastapi.models.ForecastPoint;

public interface Client {
  public List<ForecastPoint> fetchPoints(Beach beach);
}
