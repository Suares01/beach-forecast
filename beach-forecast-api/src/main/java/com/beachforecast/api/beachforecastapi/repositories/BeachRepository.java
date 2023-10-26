package com.beachforecast.api.beachforecastapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beachforecast.api.beachforecastapi.entities.Beach;

import java.util.List;

public interface BeachRepository extends JpaRepository<Beach, UUID> {
  List<Beach> findByUserId(UUID userId);
  long countByUserId(UUID userId);
  boolean existsByLatAndLngAndUserId(float lat, float lng, UUID userId);
  boolean existsByNameAndUserId(String name, UUID userId);
}
