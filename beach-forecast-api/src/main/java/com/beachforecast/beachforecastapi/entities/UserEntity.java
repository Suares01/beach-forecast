package com.beachforecast.beachforecastapi.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity implements UserDetails {
  private static final long serialVersionUID = 2L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(nullable = false, length = 50)
  private String firstName;

  @Column(length = 50)
  private String lastName;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private UserRole role;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<BeachEntity> beaches = new ArrayList<BeachEntity>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RatingEntity> ratings = new ArrayList<>();

  @Column(nullable = false)
  private Date createdAt;

  public enum UserRole {
    ADMIN("admin"),
    USER("user");

    private String role;

    UserRole(String role) {
      this.role = role;
    }

    public String getRole() {
      return role;
    }
  }

  public record FormatUser(
      UUID id,
      String firstName,
      String lastName,
      String email,
      UserRole role,
      Date createdAt) {
  }

  public UserEntity() {
  }

  public FormatUser formatUser() {
    return new FormatUser(id, firstName, lastName, email, role, createdAt);
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UserRole getRole() {
    return this.role;
  }

  public void setRole(UserRole role) {
    this.role = role;
  }

  public List<BeachEntity> getBeaches() {
    return this.beaches;
  }

  public void setBeaches(List<BeachEntity> beaches) {
    this.beaches = beaches;
  }

  public List<RatingEntity> getRatings() {
    return this.ratings;
  }

  public void setRatings(List<RatingEntity> ratings) {
    this.ratings = ratings;
  }

  public Date getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (role == UserRole.ADMIN) {
      return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
    } else {
      return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
