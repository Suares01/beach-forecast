package com.beachforecast.api.beachforecastapi.infra.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GrantType {
  AUTHORIZATION_CODE("authorization_code"),
  IMPLICIT("implicit"),
  REFRESH_TOKEN("refresh_token"),
  PASSWORD("password");

  private String type;
}
