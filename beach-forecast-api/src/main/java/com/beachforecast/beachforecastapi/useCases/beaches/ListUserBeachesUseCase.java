package com.beachforecast.beachforecastapi.useCases.beaches;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beachforecast.beachforecastapi.entities.BeachEntity;
import com.beachforecast.beachforecastapi.entities.UserEntity;
import com.beachforecast.beachforecastapi.repositories.BeachRepository;

@Service
public class ListUserBeachesUseCase {
  @Autowired
  private BeachRepository beachRepository;

  public List<BeachEntity> execute(UserEntity user) {
    List<BeachEntity> beaches = beachRepository.findByUser(user);

    return beaches;
  }
}
