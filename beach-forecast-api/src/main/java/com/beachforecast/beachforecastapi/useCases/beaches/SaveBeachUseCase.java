package com.beachforecast.beachforecastapi.useCases.beaches;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beachforecast.beachforecastapi.dtos.BeachRecordDto;
import com.beachforecast.beachforecastapi.entities.BeachEntity;
import com.beachforecast.beachforecastapi.entities.UserEntity;
import com.beachforecast.beachforecastapi.repositories.BeachRepository;

import jakarta.transaction.Transactional;

@Service
public class SaveBeachUseCase {
  @Autowired
  private BeachRepository beachRepository;

  @Transactional
  public BeachEntity execute(BeachRecordDto beachRecordDto, UserEntity user) {
    var beach = new BeachEntity();
    BeanUtils.copyProperties(beachRecordDto, beach);
    beach.setUser(user);
    beach.setCreatedAt(new Date());

    return beachRepository.save(beach);
  }
}
