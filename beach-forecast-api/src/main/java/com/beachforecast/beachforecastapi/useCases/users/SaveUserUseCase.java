package com.beachforecast.beachforecastapi.useCases.users;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.beachforecast.beachforecastapi.dtos.CriterionDto;
import com.beachforecast.beachforecastapi.dtos.UserRecordDto;
import com.beachforecast.beachforecastapi.entities.CriterionEntity;
import com.beachforecast.beachforecastapi.entities.RatingEntity;
import com.beachforecast.beachforecastapi.entities.UserEntity;
import com.beachforecast.beachforecastapi.entities.CriterionEntity.CriterionType;
import com.beachforecast.beachforecastapi.entities.RatingEntity.RatingType;
import com.beachforecast.beachforecastapi.entities.UserEntity.UserRole;
import com.beachforecast.beachforecastapi.errors.ConflictException;
import com.beachforecast.beachforecastapi.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class SaveUserUseCase {

  @Autowired
  private UserRepository userRepository;

  @Transactional
  public UserEntity execute(UserRecordDto userDto) throws ConflictException {
    boolean emailAlreadyRegistered = userRepository.existsByEmail(userDto.email());

    if (emailAlreadyRegistered) {
      throw new ConflictException("Email has already registered.");
    }

    String encryptedPassword = new BCryptPasswordEncoder().encode(userDto.password());

    var userEntity = new UserEntity();
    BeanUtils.copyProperties(userDto, userEntity);
    userEntity.setPassword(encryptedPassword);
    userEntity.setRole(UserRole.USER);
    userEntity.setCreatedAt(new Date());

    List<RatingEntity> ratingEntities = new ArrayList<>();

    ratingEntities.add(generateRatingForSurfer(userEntity));
    ratingEntities.add(generateRatingForBeachgoer(userEntity));

    userEntity.setRatings(ratingEntities);

    return userRepository.save(userEntity);
  }

  private RatingEntity generateRatingForSurfer(UserEntity user) {
    var rating = new RatingEntity();

    rating.setName("Avaliação de surf");
    rating.setType(RatingType.SURFER);
    rating.setCriteria(generateCriteriaForSurfer(rating));
    rating.setUser(user);
    rating.setCreatedAt(new Date());

    return rating;
  }

  private RatingEntity generateRatingForBeachgoer(UserEntity user) {
    var rating = new RatingEntity();

    rating.setName("Avaliação de banhista");
    rating.setType(RatingType.BEACHGOER);
    rating.setCriteria(generateCriteriaForBeachgoer(rating));
    rating.setUser(user);
    rating.setCreatedAt(new Date());

    return rating;
  }

  private List<CriterionEntity> generateCriteriaForSurfer(RatingEntity rating) {
    List<CriterionEntity> criteria = new ArrayList<>();

    criteria.add(createCriterionInstance(new CriterionDto("Altura das ondas", CriterionType.WAVE_HEIGHT, 0.8), rating));
    criteria.add(createCriterionInstance(new CriterionDto("Direção das ondas", CriterionType.WAVE_DIRECTION, 0.9), rating));
    criteria.add(createCriterionInstance(new CriterionDto("Período das ondas", CriterionType.WAVE_PERIOD, 0.6), rating));
    criteria.add(createCriterionInstance(new CriterionDto("Altura das ondas de swell", CriterionType.SWELL_HEIGHT, 0.7), rating));
    criteria.add(createCriterionInstance(new CriterionDto("Direção das ondas de swell", CriterionType.SWELL_DIRECTION, 0.9), rating));
    criteria.add(createCriterionInstance(new CriterionDto("Período das ondas de swell", CriterionType.SWELL_PERIOD, 0.6), rating));
    criteria.add(createCriterionInstance(new CriterionDto("Direção do vento", CriterionType.WIND_DIRECTION, 0.5), rating));

    return criteria;
  }

  private List<CriterionEntity> generateCriteriaForBeachgoer(RatingEntity rating) {
    List<CriterionEntity> criteria = new ArrayList<>();

    criteria.add(createCriterionInstance(new CriterionDto("Altura das ondas", CriterionType.WAVE_HEIGHT, 0.6), rating));
    criteria.add(createCriterionInstance(new CriterionDto("Direção das ondas", CriterionType.WAVE_DIRECTION, 0.7), rating));
    criteria.add(createCriterionInstance(new CriterionDto("Período das ondas", CriterionType.WAVE_PERIOD, 0.4), rating));
    criteria.add(createCriterionInstance(new CriterionDto("Altura das ondas de swell", CriterionType.SWELL_HEIGHT, 0.5), rating));
    criteria.add(createCriterionInstance(new CriterionDto("Direção das ondas de swell", CriterionType.SWELL_DIRECTION, 0.6), rating));
    criteria.add(createCriterionInstance(new CriterionDto("Período das ondas de swell", CriterionType.SWELL_PERIOD, 0.4), rating));
    criteria.add(createCriterionInstance(new CriterionDto("Direção do vento", CriterionType.WIND_DIRECTION, 0.3), rating));

    return criteria;
  }

  private CriterionEntity createCriterionInstance(CriterionDto criterionDto, RatingEntity rating) {
    var criterion = new CriterionEntity();
    criterion.setName(criterionDto.name());
    criterion.setType(criterionDto.type());
    criterion.setWeight(criterionDto.weight());
    criterion.setRating(rating);
    criterion.setCreatedAt(new Date());

    return criterion;
  }
}
