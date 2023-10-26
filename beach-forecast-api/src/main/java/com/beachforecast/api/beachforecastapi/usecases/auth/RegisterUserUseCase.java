package com.beachforecast.api.beachforecastapi.usecases.auth;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.beachforecast.api.beachforecastapi.dtos.RegisterUserDto;
import com.beachforecast.api.beachforecastapi.entities.Rating;
import com.beachforecast.api.beachforecastapi.models.Credentials;
import com.beachforecast.api.beachforecastapi.models.Tokens;
import com.beachforecast.api.beachforecastapi.models.UserData;
import com.beachforecast.api.beachforecastapi.models.UserRepresentation;
import com.beachforecast.api.beachforecastapi.repositories.RatingRepository;
import com.beachforecast.api.beachforecastapi.services.KeycloakAdminApiService;
import com.beachforecast.api.beachforecastapi.utils.DefaultRatings;

@Service
public class RegisterUserUseCase {
  @Autowired
  private KeycloakAdminApiService keycloakAdminApiService;

  @Autowired
  private RatingRepository ratingRepository;

  @Value("${api.security.keycloak-base-url}")
  private String baseUrl;

  @Value("${api.security.keycloak-realm}")
  private String realm;

  public void execute(RegisterUserDto data) {
    Tokens tokens = keycloakAdminApiService.retrieveAdminTokens();

    var client = WebClient
        .builder()
        .baseUrl(baseUrl)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeaders(header -> header.setBearerAuth(tokens.getAccessToken()))
        .build();

    var credentials = new Credentials("password", data.password());
    var userRepresentation = new UserRepresentation(
        data.username(),
        data.email(),
        false,
        true,
        data.firstName(),
        data.lastName(),
        Set.of(credentials));

    String uri = "/admin/realms/" + realm + "/users";

    client
        .method(HttpMethod.POST)
        .uri(uri)
        .body(BodyInserters.fromValue(userRepresentation))
        .retrieve()
        .bodyToMono(String.class)
        .doOnSuccess(response -> {
          client
              .method(HttpMethod.GET)
              .uri(builder -> builder
                  .path(uri)
                  .queryParam("username", data.username())
                  .build())
              .accept(MediaType.APPLICATION_JSON)
              .retrieve()
              .bodyToFlux(UserData.class)
              .collectList()
              .doOnSuccess(users -> {
                users.stream().forEach(user -> {
                  Set<Rating> ratings = DefaultRatings.generateDefaultRatings(user.getId());
                  ratingRepository.saveAll(ratings);
                  keycloakAdminApiService.sendVerificationEmail(user.getId(), tokens);
                });
              })
              .subscribe();
        })
        .block();
  }
}
