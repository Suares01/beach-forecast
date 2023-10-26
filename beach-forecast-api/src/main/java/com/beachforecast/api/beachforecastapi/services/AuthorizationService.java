package com.beachforecast.api.beachforecastapi.services;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.beachforecast.api.beachforecastapi.infra.security.UserInfo;

@Service
public class AuthorizationService {
  public UUID getUserId() {
    var userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userInfo.getSub();
  }

  public boolean isAuthenticated() {
    var context = SecurityContextHolder.getContext().getAuthentication();

    if (context.getName().equals("anonymousUser")) return false;

    return context.isAuthenticated();
  }

  public boolean isUnauthenticated() {
    return !isAuthenticated();
  }
}
