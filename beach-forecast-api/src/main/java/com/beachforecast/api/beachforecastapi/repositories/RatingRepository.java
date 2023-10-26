package com.beachforecast.api.beachforecastapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beachforecast.api.beachforecastapi.entities.Rating;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, UUID> {
   List<Rating> findByUserId(UUID userId);
}
