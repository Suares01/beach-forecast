package com.beachforecast.api.beachforecastapi.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterUserDto(
    @NotBlank(message = "O nome de usuário é obrigatório") String username,
    @NotBlank(message = "O email é obrigatório") @Email(message = "Email inválido") String email,
    @NotBlank(message = "A senha é obrigatória") String password,
    @NotBlank(message = "O nome é obrigatório") String firstName,
    String lastName) {

}
