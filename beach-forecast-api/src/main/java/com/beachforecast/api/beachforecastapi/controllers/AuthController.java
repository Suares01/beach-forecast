package com.beachforecast.api.beachforecastapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beachforecast.api.beachforecastapi.dtos.AuthenticationDto;
import com.beachforecast.api.beachforecastapi.dtos.RefreshTokenDto;
import com.beachforecast.api.beachforecastapi.dtos.RegisterUserDto;
import com.beachforecast.api.beachforecastapi.infra.security.UserInfo;
import com.beachforecast.api.beachforecastapi.models.Tokens;
import com.beachforecast.api.beachforecastapi.usecases.auth.AuthenticateUserUseCase;
import com.beachforecast.api.beachforecastapi.usecases.auth.RefreshTokenUseCase;
import com.beachforecast.api.beachforecastapi.usecases.auth.RegisterUserUseCase;
import com.beachforecast.api.beachforecastapi.usecases.auth.SendVerificationEmailUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthenticateUserUseCase authenticateUserUseCase;
  private final RegisterUserUseCase registerUserUseCase;
  private final SendVerificationEmailUseCase sendVerificationEmailUseCase;
  private final RefreshTokenUseCase refreshTokenUseCase;

  @PostMapping("register")
  public ResponseEntity<Void> register(@RequestBody @Valid RegisterUserDto body) {
    registerUserUseCase.execute(body);

    return ResponseEntity.status(201).build();
  }

  @PostMapping("login")
  public ResponseEntity<Tokens> login(@RequestBody @Valid AuthenticationDto body) {
    Tokens tokens = authenticateUserUseCase.execute(body);

    return ResponseEntity.ok(tokens);
  }

  @PostMapping("refresh-token")
  public ResponseEntity<Tokens> refreshToken(@RequestBody @Valid RefreshTokenDto body) {
    Tokens tokens = refreshTokenUseCase.execute(body.refreshToken());

    return ResponseEntity.ok(tokens);
  }

  @PutMapping("verify-email")
  public ResponseEntity<Void> verifyEmail(@AuthenticationPrincipal UserInfo userInfo) {
    sendVerificationEmailUseCase.execute(userInfo.getSub());

    return ResponseEntity.noContent().build();
  }
}
