package com.beachforecast.beachforecastapi.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beachforecast.beachforecastapi.entities.BeachEntity;
import com.beachforecast.beachforecastapi.entities.UserEntity;

public interface BeachRepository extends JpaRepository<BeachEntity, UUID> {
  List<BeachEntity> findByUser(UserEntity user);
}
