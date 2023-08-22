package com.beachforecast.beachforecastapi.entities;

import java.io.Serializable;
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
@Table(name = "beaches")
public class BeachEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(nullable = false, length = 40)
  private String name;

  @Column(nullable = false)
  private Double lat;

  @Column(nullable = false)
  private Double lng;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  @JsonIgnore
  private UserEntity user;

  @Column(nullable = false)
  private Position position;

  @Column(nullable = false)
  private Date createdAt;

  public enum Position {
    NORTH,
    SOUTH,
    EAST,
    WEST
  }

  public BeachEntity() {
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getLat() {
    return this.lat;
  }

  public void setLat(Double lat) {
    this.lat = lat;
  }

  public Double getLng() {
    return this.lng;
  }

  public void setLng(Double lng) {
    this.lng = lng;
  }

  public UserEntity getUser() {
    return this.user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }

  public Position getPosition() {
    return this.position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public Date getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }
}
