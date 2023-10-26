package com.beachforecast.api.beachforecastapi.usecases.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.beachforecast.api.beachforecastapi.dtos.AuthenticationDto;
import com.beachforecast.api.beachforecastapi.infra.security.GrantType;
import com.beachforecast.api.beachforecastapi.models.Tokens;

@Service
public class AuthenticateUserUseCase {
  @Value("${api.security.client-id}")
  private String clientId;

  @Value("${api.security.keycloak-base-url}")
  private String baseUrl;

  @Value("${api.security.keycloak-realm}")
  private String realm;

  private final String SCOPE = "openid";

  public Tokens execute(AuthenticationDto data) {
    var client = WebClient
        .builder()
        .baseUrl(baseUrl)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();

    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("client_id", clientId);
    formData.add("grant_type", GrantType.PASSWORD.getType());
    formData.add("username", data.username());
    formData.add("password", data.password());
    formData.add("scope", SCOPE);

    String uri = "/realms/" + realm + "/protocol/openid-connect/token";

    Tokens tokens = client
        .method(HttpMethod.POST)
        .uri(uri)
        .body(BodyInserters.fromFormData(formData))
        .retrieve()
        .bodyToMono(Tokens.class)
        .block();

    return tokens;
  }
}
