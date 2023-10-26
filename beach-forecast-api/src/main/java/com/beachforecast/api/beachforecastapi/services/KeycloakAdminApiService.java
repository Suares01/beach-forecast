package com.beachforecast.api.beachforecastapi.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.beachforecast.api.beachforecastapi.infra.security.GrantType;
import com.beachforecast.api.beachforecastapi.models.Tokens;

@Service
public class KeycloakAdminApiService {
  @Value("${api.security.keycloak-base-url}")
  private String baseUrl;

  @Value("${api.security.admin-client-id}")
  private String adminClientId;

  @Value("${api.security.admin-client-secret}")
  private String adminClientSecret;

  @Value("${api.security.admin-password}")
  private String adminPassword;

  @Value("${api.security.admin-username}")
  private String adminUsername;

  @Value("${api.security.keycloak-realm}")
  private String clientRealm;

  public Tokens retrieveAdminTokens() {
    WebClient client = buildWebClient(MediaType.APPLICATION_FORM_URLENCODED_VALUE);

    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("client_id", adminClientId);
    formData.add("grant_type", GrantType.PASSWORD.getType());
    formData.add("username", adminUsername);
    formData.add("password", adminPassword);

    return client
        .method(HttpMethod.POST)
        .uri("/realms/master/protocol/openid-connect/token")
        .body(BodyInserters.fromFormData(formData))
        .retrieve()
        .bodyToMono(Tokens.class)
        .block();
  }

  public void sendVerificationEmail(UUID userId, Tokens tokens) {
    WebClient client = buildWebClient(tokens, MediaType.APPLICATION_JSON_VALUE);

    client
        .method(HttpMethod.PUT)
        .uri("/admin/realms/" + clientRealm + "/users/" + userId + "/send-verify-email")
        .retrieve()
        .bodyToMono(String.class)
        .subscribe();
  }

  private WebClient buildWebClient(String mediaType) {
    return WebClient
        .builder()
        .baseUrl(baseUrl)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, mediaType)
        .defaultHeaders(header -> header.setBasicAuth(adminClientId, adminClientSecret))
        .build();
  }

  private WebClient buildWebClient(Tokens tokens, String mediaType) {
    return WebClient
        .builder()
        .baseUrl(baseUrl)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, mediaType)
        .defaultHeaders(header -> header.setBearerAuth(tokens.getAccessToken()))
        .build();
  }
}
