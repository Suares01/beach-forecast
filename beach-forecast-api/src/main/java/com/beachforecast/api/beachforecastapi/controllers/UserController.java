package com.beachforecast.api.beachforecastapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beachforecast.api.beachforecastapi.infra.security.UserInfo;
import com.beachforecast.api.beachforecastapi.models.UserData;
import com.beachforecast.api.beachforecastapi.usecases.user.GetUserDataUseCase;

@RestController
@RequestMapping("users")
public class UserController {
  @Autowired
  private GetUserDataUseCase getUserDataUseCase;

  @GetMapping("me")
  public ResponseEntity<UserData> getUserData(@AuthenticationPrincipal UserInfo userInfo) {
    UserData data = getUserDataUseCase.execute(userInfo.getSub());

    return ResponseEntity.ok(data);
  }
}
