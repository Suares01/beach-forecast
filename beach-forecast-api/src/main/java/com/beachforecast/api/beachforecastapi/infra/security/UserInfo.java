package com.beachforecast.api.beachforecastapi.infra.security;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
  private UUID sub;
  private boolean emailVerified;
  private String name;
  private String preferredUsername;
  private String givenName;
  private String familyName;
  private String email;
  private String role;

  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (this.role.equals("admin"))
      return Set.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
    else
      return Set.of(new SimpleGrantedAuthority("ROLE_USER"));
  }
}
