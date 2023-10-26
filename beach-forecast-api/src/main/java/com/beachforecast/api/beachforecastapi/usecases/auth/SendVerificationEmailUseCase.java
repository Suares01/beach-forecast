package com.beachforecast.api.beachforecastapi.usecases.auth;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beachforecast.api.beachforecastapi.models.Tokens;
import com.beachforecast.api.beachforecastapi.services.KeycloakAdminApiService;

@Service
public class SendVerificationEmailUseCase {
  @Autowired
  private KeycloakAdminApiService keycloakAdminApiService;

  public void execute(UUID userId) {
    Tokens tokens = keycloakAdminApiService.retrieveAdminTokens();
    keycloakAdminApiService.sendVerificationEmail(userId, tokens);
  }
}
