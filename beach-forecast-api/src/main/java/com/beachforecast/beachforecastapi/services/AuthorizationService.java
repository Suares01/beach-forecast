package com.beachforecast.beachforecastapi.services;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.beachforecast.beachforecastapi.entities.UserEntity;
import com.beachforecast.beachforecastapi.repositories.UserRepository;

import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuthorizationService implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Resource(name = "redisTemplate")
  private ValueOperations<String, UserEntity> valueOps;

  @Override
  public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
    String cacheKey = "user_details_" + username;
    UserEntity userFromCache = valueOps.get(cacheKey);

    if (userFromCache == null) {
      Optional<UserEntity> optionalUser = userRepository.findByEmail(username);

      if (optionalUser.isPresent()) {
        UserEntity user = optionalUser.get();
        valueOps.set(cacheKey, user, 60, TimeUnit.MINUTES);

        return user;
      } else {
        throw new UsernameNotFoundException("User with email " + username + " not found.");
      }
    } else {
      return userFromCache;
    }
  }

  public UUID getAuthenticatedId() {
    var context = SecurityContextHolder.getContext();
    UserEntity user = (UserEntity) context.getAuthentication().getPrincipal();
    return user.getId();
  }

  public Boolean isAuthenticated() {
    var context = SecurityContextHolder.getContext().getAuthentication();

    if (context.getName().equals("anonymousUser")) {
      return false;
    }

    return context.isAuthenticated();
  }

  public Boolean isUnauthenticated() {
    return !isAuthenticated();
  }
}
