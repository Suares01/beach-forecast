package com.beachforecast.api.beachforecastapi.usecases.beach;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.beachforecast.api.beachforecastapi.dtos.BeachDto;
import com.beachforecast.api.beachforecastapi.entities.Beach;
import com.beachforecast.api.beachforecastapi.repositories.BeachRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaveBeachUseCase {
  private final BeachRepository beachRepository;

  @Transactional
  public Beach execute(BeachDto beachRecordDto, UUID userId) throws Exception {
    long userBeachesCount = beachRepository.countByUserId(userId);

    if (userBeachesCount == 5) {
      throw new Exception("User has the maximum number of 5 beaches.");
    }

    boolean existsByLatAndLng = beachRepository.existsByLatAndLngAndUserId(beachRecordDto.lat(), beachRecordDto.lng(), userId);

    if (existsByLatAndLng) {
      throw new Exception("User already has a beach with this latitude and longitude.");
    }

    Boolean existsByName = beachRepository.existsByNameAndUserId(beachRecordDto.name(), userId);

    if (existsByName) {
      throw new Exception("User already has a beach with this name.");
    }

    var beach = new Beach();
    BeanUtils.copyProperties(beachRecordDto, beach);
    beach.setUserId(userId);
    beach.setCreatedAt(Instant.now());

    return beachRepository.save(beach);
  }
}
