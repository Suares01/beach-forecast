package com.beachforecast.beachforecastapi.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRecordDto(
    @NotBlank(message = "O primeiro nome é obrigatório") String firstName,
    String lastName,
    @NotBlank(message = "O e-mail é obrigatório") @Email(message = "E-mail inválido") String email,
    @NotBlank(message = "A senha é obrigatória") String password) {
}
