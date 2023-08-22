package com.beachforecast.beachforecastapi.entities;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "criteria")
public class CriterionEntity {
  private static final long serialVersionUID = 3L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(nullable = false, length = 40)
  private String name;

  @Column(nullable = false)
  private CriterionType type;

  @Column(nullable = false)
  private Double weight;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "rating_id", nullable = false)
  @JsonIgnore
  private RatingEntity rating;

  @Column(nullable = false)
  private Date createdAt;

  public enum CriterionType {
    WAVE_HEIGHT,
    WAVE_DIRECTION,
    WAVE_PERIOD,
    SWELL_HEIGHT,
    SWELL_DIRECTION,
    SWELL_PERIOD,
    WIND_DIRECTION
  }

  public CriterionEntity() {}

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CriterionType getType() {
    return type;
  }

  public void setType(CriterionType type) {
    this.type = type;
  }

  public Double getWeight() {
    return weight;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
  }

  public RatingEntity getRating() {
    return rating;
  }

  public void setRating(RatingEntity rating) {
    this.rating = rating;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }
}
