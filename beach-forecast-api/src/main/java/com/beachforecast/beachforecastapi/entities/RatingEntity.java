package com.beachforecast.beachforecastapi.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ratings")
public class RatingEntity {
  private static final long serialVersionUID = 4L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(nullable = false, length = 40)
  private String name;

  @Column(nullable = false)
  private RatingType type;

  @OneToMany(mappedBy = "rating", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CriterionEntity> criteria = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  @JsonIgnore
  private UserEntity user;

  @Column(nullable = false)
  private Date createdAt;

  public enum RatingType {
    SURFER,
    BEACHGOER
  }

  public RatingEntity() {
  }

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

  public RatingType getType() {
    return type;
  }

  public void setType(RatingType type) {
    this.type = type;
  }

  public List<CriterionEntity> getCriteria() {
    return criteria;
  }

  public void setCriteria(List<CriterionEntity> criteria) {
    this.criteria = criteria;
  }

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

}
