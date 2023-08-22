package com.beachforecast.beachforecastapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beachforecast.beachforecastapi.entities.CriterionEntity;

public interface CriterionRepository extends JpaRepository<CriterionEntity, UUID> {
}
