package com.beachforecast.api.beachforecastapi.utils;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import com.beachforecast.api.beachforecastapi.dtos.CriterionDto;
import com.beachforecast.api.beachforecastapi.entities.Criterion;
import com.beachforecast.api.beachforecastapi.entities.Rating;
import com.beachforecast.api.beachforecastapi.entities.Criterion.CriterionType;
import com.beachforecast.api.beachforecastapi.entities.Rating.RatingType;

public class DefaultRatings {
  public static Set<Rating> generateDefaultRatings(UUID uuid) {
    return Set.of(
        generateRatingForSurfer(uuid),
        generateRatingForBeachgoer(uuid));
  }

  private static Rating generateRatingForSurfer(UUID uuid) {
    var rating = new Rating();

    rating.setName("Avaliação de surf");
    rating.setType(RatingType.SURFER);
    rating.setCriteria(generateCriteriaForSurfer(rating));
    rating.setUserId(uuid);
    rating.setCreatedAt(Instant.now());

    return rating;
  }

  private static Rating generateRatingForBeachgoer(UUID uuid) {
    var rating = new Rating();

    rating.setName("Avaliação de banhista");
    rating.setType(RatingType.BEACHGOER);
    rating.setCriteria(generateCriteriaForBeachgoer(rating));
    rating.setUserId(uuid);
    rating.setCreatedAt(Instant.now());

    return rating;
  }

  private static Set<Criterion> generateCriteriaForSurfer(Rating rating) {
    return Set.of(
        createCriterionInstance(new CriterionDto("Altura das ondas", CriterionType.WAVE_HEIGHT, 0.8F), rating),
        createCriterionInstance(new CriterionDto("Direção das ondas", CriterionType.WAVE_DIRECTION, 0.9F), rating),
        createCriterionInstance(new CriterionDto("Período das ondas", CriterionType.WAVE_PERIOD, 0.6F), rating),
        createCriterionInstance(new CriterionDto("Altura das ondas de swell", CriterionType.SWELL_HEIGHT, 0.7F), rating),
        createCriterionInstance(new CriterionDto("Direção das ondas de swell", CriterionType.SWELL_DIRECTION, 0.9F), rating),
        createCriterionInstance(new CriterionDto("Período das ondas de swell", CriterionType.SWELL_PERIOD, 0.6F), rating),
        createCriterionInstance(new CriterionDto("Direção do vento", CriterionType.WIND_DIRECTION, 0.5F), rating));
  }

  private static Set<Criterion> generateCriteriaForBeachgoer(Rating rating) {
    return Set.of(
        createCriterionInstance(new CriterionDto("Altura das ondas", CriterionType.WAVE_HEIGHT, 0.6F), rating),
        createCriterionInstance(new CriterionDto("Direção das ondas", CriterionType.WAVE_DIRECTION, 0.7F), rating),
        createCriterionInstance(new CriterionDto("Período das ondas", CriterionType.WAVE_PERIOD, 0.4F), rating),
        createCriterionInstance(new CriterionDto("Altura das ondas de swell", CriterionType.SWELL_HEIGHT, 0.5F), rating),
        createCriterionInstance(new CriterionDto("Direção das ondas de swell", CriterionType.SWELL_DIRECTION, 0.6F), rating),
        createCriterionInstance(new CriterionDto("Período das ondas de swell", CriterionType.SWELL_PERIOD, 0.4F), rating),
        createCriterionInstance(new CriterionDto("Direção do vento", CriterionType.WIND_DIRECTION, 0.3F), rating));
  }

  private static Criterion createCriterionInstance(CriterionDto criterionDto, Rating rating) {
    var criterion = new Criterion();
    criterion.setName(criterionDto.name());
    criterion.setType(criterionDto.type());
    criterion.setWeight(criterionDto.weight());
    criterion.setRating(rating);
    criterion.setCreatedAt(Instant.now());

    return criterion;
  }
}
