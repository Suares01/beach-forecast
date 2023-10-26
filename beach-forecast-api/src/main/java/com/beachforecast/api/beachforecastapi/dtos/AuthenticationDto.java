package com.beachforecast.api.beachforecastapi.dtos;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationDto(
    @NotBlank(message = "O nome de usuário é obrigatório") String username,
    @NotBlank(message = "A senha é obrigatória") String password) {

}
