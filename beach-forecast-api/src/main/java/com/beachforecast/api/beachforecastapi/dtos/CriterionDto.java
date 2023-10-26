package com.beachforecast.api.beachforecastapi.dtos;

import com.beachforecast.api.beachforecastapi.entities.Criterion.CriterionType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriterionDto(
    @NotBlank(message = "O nome do critério é obrigaório.") String name,
    @NotNull(message = "O tipo do critério é obrigatório.") @Enumerated(EnumType.STRING) CriterionType type,
    @NotNull(message = "O peso do critério é obrigatório.") @DecimalMin(value = "0", message = "O peso mínimo é 0") @DecimalMax(value = "1", message = "O peso máximo é 1") float weight) {
}
