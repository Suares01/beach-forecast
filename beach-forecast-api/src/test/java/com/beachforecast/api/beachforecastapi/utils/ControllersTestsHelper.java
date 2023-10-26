package com.beachforecast.api.beachforecastapi.utils;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;

import com.beachforecast.api.beachforecastapi.dtos.BeachDto;
import com.beachforecast.api.beachforecastapi.entities.Beach;
import com.beachforecast.api.beachforecastapi.entities.Beach.Position;
import com.beachforecast.api.beachforecastapi.infra.security.UserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ControllersTestsHelper {
  public static final ObjectMapper objectMapper = new ObjectMapper();

  public static UserInfo generatUserInfo() {
    var userInfo = new UserInfo(UUID.randomUUID(), true, "John Doe", "john", "John", "Doe", "johndoe@mail.com", "user");
    return userInfo;
  }

  public static Beach generateBeach(UserInfo userInfo) {
    var beach = new Beach(UUID.randomUUID(), "Copacabana", 40, 100, userInfo.getSub(), Position.SOUTH, Instant.now());
    return beach;
  }

  public static BeachDto generateBeachDto() {
    return new BeachDto("Copacabana", 40, 100, Position.SOUTH);
  }

  public static JwtRequestPostProcessor jwtRequestPostProcessor(UserInfo userInfo) {
    Map<String, String> jwtHeaders = new HashMap<>();
    jwtHeaders.put("alg", "RS256");
    jwtHeaders.put("typ", "JWT");
    jwtHeaders.put("kid", "8kCLYTC6AqYrQ8QU3Avn0HOt6zPmuo5DE1o0ecvc5wA");

    Map<String, Object> realmAccess = Map.of("roles", List.of(userInfo.getRole()));

    Map<String, Object> claimsMap = new HashMap<>();
    claimsMap.put("realm_access", realmAccess);
    claimsMap.put("email", userInfo.getEmail());
    claimsMap.put("given_name", userInfo.getGivenName());
    claimsMap.put("family_name", userInfo.getFamilyName());
    claimsMap.put("preferred_username", userInfo.getPreferredUsername());
    claimsMap.put("email_verified", userInfo.isEmailVerified());
    claimsMap.put("name", userInfo.getName());
    claimsMap.put("scope", "openid email profile");

    return SecurityMockMvcRequestPostProcessors.jwt()
        .jwt(jwt -> jwt
            .headers(headers -> headers.putAll(jwtHeaders))
            .claims(claims -> claims.putAll(claimsMap))
            .subject(userInfo.getSub().toString()));
  }
}
