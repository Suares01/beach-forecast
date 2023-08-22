package com.beachforecast.beachforecastapi.dtos;

import java.util.List;

import com.beachforecast.beachforecastapi.entities.RatingEntity.RatingType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RatingDto(
    @NotBlank(message = "O nome da avaliação é obrigaório.") String name,
    @NotNull(message = "O tipo da avaliação é obrigatório.") @Enumerated(EnumType.STRING) RatingType type,
    @NotNull(message = "A lista de critérios é obrigatória.") @NotEmpty(message = "A lista de critérios não pode ser vazia.") List<@Valid CriterionDto> criteria) {
}
