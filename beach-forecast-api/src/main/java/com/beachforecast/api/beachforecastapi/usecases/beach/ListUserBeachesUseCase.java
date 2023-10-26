package com.beachforecast.api.beachforecastapi.usecases.beach;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.beachforecast.api.beachforecastapi.entities.Beach;
import com.beachforecast.api.beachforecastapi.repositories.BeachRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListUserBeachesUseCase {
  private final BeachRepository beachRepository;

  public List<Beach> execute(UUID userId) {
    List<Beach> beaches = beachRepository.findByUserId(userId);

    return beaches;
  }
}
