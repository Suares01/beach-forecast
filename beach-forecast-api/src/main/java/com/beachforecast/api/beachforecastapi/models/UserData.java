package com.beachforecast.api.beachforecastapi.models;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
  private UUID id;
  private String username;
  private boolean enabled;
  private boolean emailVerified;
  private String email;
  private String firstName;
  private String lastName;
}
