package com.beachforecast.api.beachforecastapi.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ratings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rating implements Serializable {
  private static final long serialVersionUID = 242424643L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(nullable = false, length = 40)
  private String name;

  @Column(nullable = false)
  private RatingType type;

  @OneToMany(mappedBy = "rating", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Criterion> criteria = new HashSet<Criterion>(0);

  @Column(nullable = false)
  private UUID userId;

  @Column(nullable = false)
  private Instant createdAt;

  @Getter
  public enum RatingType {
    SURFER("SURFER"),
    BEACHGOER("BEACHGOER");

    private String type;

    RatingType(String type) {
      this.type = type;
    }
  }
}
