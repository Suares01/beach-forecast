package com.beachforecast.beachforecastapi.useCases.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.beachforecast.beachforecastapi.dtos.AuthenticationDto;
import com.beachforecast.beachforecastapi.entities.UserEntity;
import com.beachforecast.beachforecastapi.infra.security.TokenService;

@Service
public class LoginUseCase {
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private TokenService tokenService;

  private record LoginResponseRecord(String token) {
  }

  public LoginResponseRecord execute(AuthenticationDto data) {
    var authentication = new UsernamePasswordAuthenticationToken(data.email(), data.password());
    var auth = authenticationManager.authenticate(authentication);

    var token = tokenService.generateToken((UserEntity) auth.getPrincipal());
    var response = new LoginResponseRecord(token);

    return response;
  }
}
