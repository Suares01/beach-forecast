package com.beachforecast.beachforecastapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beachforecast.beachforecastapi.dtos.AuthenticationDto;
import com.beachforecast.beachforecastapi.dtos.UserRecordDto;
import com.beachforecast.beachforecastapi.entities.UserEntity;
import com.beachforecast.beachforecastapi.useCases.authentication.LoginUseCase;
import com.beachforecast.beachforecastapi.useCases.users.SaveUserUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
  @Autowired
  private SaveUserUseCase saveUserUseCase;

  @Autowired
  private LoginUseCase loginUseCase;

  @PostMapping("login")
  public ResponseEntity<Object> login(@RequestBody @Valid AuthenticationDto data) {
    final var token = loginUseCase.execute(data);

    return ResponseEntity.ok(token);
  }

  @PostMapping("register")
  public ResponseEntity<Object> register(@RequestBody @Valid UserRecordDto userDto) {
    final UserEntity user = saveUserUseCase.execute(userDto);

    return ResponseEntity.status(HttpStatus.CREATED).body(user.formatUser());
  }
}
