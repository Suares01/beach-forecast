package com.beachforecast.api.beachforecastapi.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "beaches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Beach implements Serializable {
  private static final long serialVersionUID = 8218327464L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(nullable = false, length = 40)
  private String name;

  @Column(nullable = false)
  private float lat;

  @Column(nullable = false)
  private float lng;

  @Column(nullable = false)
  private UUID userId;

  @Column(nullable = false)
  private Position position;

  @Column(nullable = false)
  private Instant createdAt;

  public enum Position {
    NORTH("NORTH"),
    SOUTH("SOUTH"),
    EAST("EAST"),
    WEST("WEST");

    Position(String position) {
    }
  }
}
