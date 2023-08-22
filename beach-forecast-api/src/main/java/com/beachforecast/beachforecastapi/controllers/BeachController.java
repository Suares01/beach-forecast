package com.beachforecast.beachforecastapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beachforecast.beachforecastapi.dtos.BeachRecordDto;
import com.beachforecast.beachforecastapi.entities.BeachEntity;
import com.beachforecast.beachforecastapi.entities.UserEntity;
import com.beachforecast.beachforecastapi.useCases.beaches.ListUserBeachesUseCase;
import com.beachforecast.beachforecastapi.useCases.beaches.SaveBeachUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("beaches")
public class BeachController {
  @Autowired
  private SaveBeachUseCase saveBeachUseCase;

  @Autowired
  private ListUserBeachesUseCase listUserBeachesUseCase;

  @GetMapping
  public ResponseEntity<Object> listUserBeaches() {
    final var user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    final var beaches = listUserBeachesUseCase.execute(user);

    return ResponseEntity.ok(beaches);
  }

  @PostMapping
  public ResponseEntity<Object> saveBeach(@RequestBody @Valid BeachRecordDto beachRecordDto) {
    final var user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    final BeachEntity beach = saveBeachUseCase.execute(beachRecordDto, user);

    return ResponseEntity.status(HttpStatus.CREATED).body(beach);
  }
}
