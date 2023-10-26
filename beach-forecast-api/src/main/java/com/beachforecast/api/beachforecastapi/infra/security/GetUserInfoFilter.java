package com.beachforecast.api.beachforecastapi.infra.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class GetUserInfoFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.isAuthenticated()) {
      var jwt = (Jwt) authentication.getPrincipal();

      UUID sub = UUID.fromString(jwt.getSubject());
      boolean emailVerified = jwt.getClaimAsBoolean("email_verified");
      String name = jwt.getClaimAsString("name");
      String preferredUsername = jwt.getClaimAsString("preferred_username");
      String givenName = jwt.getClaimAsString("given_name");
      String familyName = jwt.getClaimAsString("family_name");
      String email = jwt.getClaimAsString("email");
      String userRole = getUserRole(jwt);

      var userInfo = new UserInfo(sub, emailVerified, name, preferredUsername, givenName, familyName, email, userRole);

      var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userInfo, null, userInfo.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    filterChain.doFilter(request, response);
  }

  private String getUserRole(Jwt jwt) {
    Map<String, Collection<String>> realmAccess = jwt.getClaim("realm_access");
    String userRole = realmAccess
        .get("roles")
        .stream()
        .filter(role -> role.equals("user") || role.equals("admin"))
        .collect(Collectors.toList())
        .get(0);

    return userRole;
  }

}
