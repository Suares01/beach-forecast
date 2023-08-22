package com.beachforecast.beachforecastapi.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beachforecast.beachforecastapi.entities.RatingEntity;
import com.beachforecast.beachforecastapi.entities.UserEntity;

public interface RatingRepository extends JpaRepository<RatingEntity, UUID> {
  List<RatingEntity> findByUser(UserEntity user);
}
