package com.beachforecast.api.beachforecastapi.controllers;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beachforecast.api.beachforecastapi.infra.security.UserInfo;
import com.beachforecast.api.beachforecastapi.models.TimeForecast;
import com.beachforecast.api.beachforecastapi.usecases.forecast.ProcessForecastsForBeachesUseCase;

@CrossOrigin
@RestController
@RequestMapping("forecasts")
public class ForecastController {
  @Autowired
  private ProcessForecastsForBeachesUseCase processForecastsForBeachesUseCase;

  @GetMapping
  public ResponseEntity<Object> getForecasts(@AuthenticationPrincipal UserInfo userInfo) throws ExecutionException, InterruptedException {
    List<TimeForecast> forecasts = processForecastsForBeachesUseCase.execute(userInfo.getSub());

    return ResponseEntity.ok(forecasts);
  }
}
