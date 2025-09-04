package br.com.atypical.Softmind.Employee.dto;

public record EmployeeCreateDto(
        String companyId,
        String name,
        String email,
        String role,
        String sector
) {}
