package com.beachforecast.api.beachforecastapi.models;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRepresentation {
  private String username;
  private String email;
  private boolean emailVerified;
  private boolean enabled;
  private String firstName;
  private String lastName;
  private Set<Credentials> credentials;
}
