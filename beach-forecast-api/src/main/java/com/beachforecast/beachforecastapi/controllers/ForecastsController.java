package com.beachforecast.beachforecastapi.controllers;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beachforecast.beachforecastapi.entities.UserEntity;
import com.beachforecast.beachforecastapi.useCases.forecasts.ProcessForecastsForBeachesUseCase;

@RestController
@RequestMapping("forecasts")
public class ForecastsController {
  @Autowired
  private ProcessForecastsForBeachesUseCase processForecastsForBeachesUseCase;

  @GetMapping
  public ResponseEntity<Object> processForecasts() throws InterruptedException, ExecutionException {
    final var user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    final var forecasts = processForecastsForBeachesUseCase.execute(user);

    return ResponseEntity.ok(forecasts);
  }
}
