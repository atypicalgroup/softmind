package br.com.atypical.Softmind.Security.dto;

public record LoginPendingChangeDto(
        String username,
        String message
) {}
