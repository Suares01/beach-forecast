package com.beachforecast.api.beachforecastapi.dtos;

import com.beachforecast.api.beachforecastapi.entities.Beach.Position;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BeachDto(
    @NotBlank(message = "O nome da praia é obrigatório") String name,
    @NotNull(message = "A latitude é obrigatória") @DecimalMin(value = "-90.0", message = "A latitude mínima é -90.0") @DecimalMax(value = "90.0", message = "A latitude máxima é 90.0") float lat,
    @NotNull(message = "A longitude é obrigatória") @DecimalMin(value = "-180.0", message = "A longitude mínima é -180.0") @DecimalMax(value = "180.0", message = "A longitude máxima é 180.0") float lng,
    @NotNull(message = "A posição é obrigatória") @Enumerated(EnumType.STRING) Position position) {

}
