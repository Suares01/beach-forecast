package com.beachforecast.api.beachforecastapi.dtos;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenDto(@NotBlank(message = "O refresh token é obrigatório.") String refreshToken) {

}
