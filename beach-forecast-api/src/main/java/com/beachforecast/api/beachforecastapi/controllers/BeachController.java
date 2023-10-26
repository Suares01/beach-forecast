package com.beachforecast.api.beachforecastapi.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beachforecast.api.beachforecastapi.dtos.BeachDto;
import com.beachforecast.api.beachforecastapi.entities.Beach;
import com.beachforecast.api.beachforecastapi.infra.security.UserInfo;
import com.beachforecast.api.beachforecastapi.usecases.beach.ListUserBeachesUseCase;
import com.beachforecast.api.beachforecastapi.usecases.beach.SaveBeachUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("beaches")
@RequiredArgsConstructor
public class BeachController {
  private final ListUserBeachesUseCase listUserBeachesUseCase;
  private final SaveBeachUseCase saveBeachUseCase;

  @GetMapping
  public ResponseEntity<Object> listUserBeaches(@AuthenticationPrincipal UserInfo userInfo) {
    List<Beach> beaches = listUserBeachesUseCase.execute(userInfo.getSub());

    return ResponseEntity.ok(beaches);
  }

  @PostMapping
  public ResponseEntity<Object> saveBeach(@RequestBody @Valid BeachDto body, @AuthenticationPrincipal UserInfo userInfo) throws Exception {
    Beach beach = saveBeachUseCase.execute(body, userInfo.getSub());

    return ResponseEntity.status(201).body(beach);
  }
}
