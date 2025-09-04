package br.com.atypical.Softmind.Employee.dto;

import java.time.LocalDateTime;

public record EmployeeDto(
        String id,
        String companyId,
        String name,
        String email,
        String role,
        String sector,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean active
) {}
