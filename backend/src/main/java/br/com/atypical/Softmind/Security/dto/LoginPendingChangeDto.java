package br.com.atypical.Softmind.Security.dto;

public record LoginPendingChangeDto(
        String userId,
        String username,
        String message
) {}
