package com.beachforecast.api.beachforecastapi.entities;

import java.io.Serializable;
import java.time.Instant;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "criteria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Criterion implements Serializable {
  private static final long serialVersionUID = 2101460038L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(nullable = false, length = 40)
  private String name;

  @Column(nullable = false)
  private CriterionType type;

  @Column(nullable = false)
  private float weight;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "rating_id", nullable = false)
  @JsonIgnore
  private Rating rating;

  @Column(nullable = false)
  private Instant createdAt;

  @Getter
  public enum CriterionType {
    WAVE_HEIGHT("WAVE_HEIGHT"),
    WAVE_DIRECTION("WAVE_DIRECTION"),
    WAVE_PERIOD("WAVE_PERIOD"),
    SWELL_HEIGHT("SWELL_HEIGHT"),
    SWELL_DIRECTION("SWELL_DIRECTION"),
    SWELL_PERIOD("SWELL_PERIOD"),
    WIND_DIRECTION("WIND_DIRECTION");

    private String criterion;

    CriterionType(String criterion) {
    }
  }
}
