package com.beachforecast.api.beachforecastapi.usecases.user;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.beachforecast.api.beachforecastapi.models.Tokens;
import com.beachforecast.api.beachforecastapi.models.UserData;
import com.beachforecast.api.beachforecastapi.services.KeycloakAdminApiService;

@Service
public class GetUserDataUseCase {
  @Autowired
  private KeycloakAdminApiService keycloakAdminApiService;

  @Value("${api.security.keycloak-base-url}")
  private String baseUrl;

  @Value("${api.security.keycloak-realm}")
  private String realm;

  public UserData execute(UUID id) {
    Tokens tokens = keycloakAdminApiService.retrieveAdminTokens();

    var client = WebClient
        .builder()
        .baseUrl(baseUrl)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeaders(header -> header.setBearerAuth(tokens.getAccessToken()))
        .build();

    String uri = "/admin/realms/" + realm + "/users/" + id;

    UserData userData = client
        .method(HttpMethod.GET)
        .uri(uri)
        .retrieve()
        .bodyToMono(UserData.class)
        .block();

    return userData;
  }
}
