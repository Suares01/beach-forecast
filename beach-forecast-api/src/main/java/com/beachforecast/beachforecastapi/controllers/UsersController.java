package com.beachforecast.beachforecastapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beachforecast.beachforecastapi.entities.UserEntity;

@RestController
@RequestMapping("users")
public class UsersController {
  @GetMapping("me")
  public ResponseEntity<Object> getAuthUserData() {
    final var user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    return ResponseEntity.ok(user.formatUser());
  }
}
