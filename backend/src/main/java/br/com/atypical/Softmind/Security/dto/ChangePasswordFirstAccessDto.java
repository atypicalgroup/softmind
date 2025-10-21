package br.com.atypical.Softmind.Security.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordFirstAccessDto(
        @NotBlank String userId,
        @NotBlank String newPassword
) {}
