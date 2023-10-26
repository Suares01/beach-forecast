package com.beachforecast.api.beachforecastapi.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Credentials {
  private String type;
  private String value;
}
