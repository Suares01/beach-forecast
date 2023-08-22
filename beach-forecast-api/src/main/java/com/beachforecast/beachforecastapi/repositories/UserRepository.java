package com.beachforecast.beachforecastapi.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beachforecast.beachforecastapi.entities.UserEntity;


public interface UserRepository extends JpaRepository<UserEntity, UUID> {
  boolean existsByEmail(String email);
  Optional<UserEntity> findByEmail(String email);
}
