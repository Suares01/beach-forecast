package com.beachforecast.api.beachforecastapi.models;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.beachforecast.api.beachforecastapi.entities.Criterion;
import com.beachforecast.api.beachforecastapi.entities.Rating.RatingType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ForecastRating {
  private UUID id;
  private String name;
  private RatingType type;
  private Set<Criterion> criteria = new HashSet<Criterion>(0);
  private int value;
}
