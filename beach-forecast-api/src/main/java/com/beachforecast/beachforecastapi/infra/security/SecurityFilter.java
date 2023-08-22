package com.beachforecast.beachforecastapi.infra.security;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.beachforecast.beachforecastapi.entities.UserEntity;
import com.beachforecast.beachforecastapi.repositories.UserRepository;

import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
  @Autowired
  TokenService tokenService;

  @Autowired
  UserRepository userRepository;

  @Resource(name = "redisTemplate")
  private ValueOperations<String, UserEntity> valueOps;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    var optionalToken = recoverToken(request);

    if (optionalToken.isPresent()) {
      String token = optionalToken.get();
      String email = tokenService.validateToken(token);

      String cacheKey = "user_details_" + email;
      UserEntity userFromCache = valueOps.get(cacheKey);

      if (userFromCache == null) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
          UserEntity user = optionalUser.get();
          valueOps.set(cacheKey, user, 60, TimeUnit.MINUTES);

          var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      } else {
        var authentication = new UsernamePasswordAuthenticationToken(userFromCache, null, userFromCache.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }

    filterChain.doFilter(request, response);
  }

  private Optional<String> recoverToken(HttpServletRequest request) {
    var authHeader = request.getHeader("Authorization");

    if (authHeader == null) {
      return Optional.empty();
    }

    return Optional.of(authHeader.replace("Bearer ", ""));
  }
}
