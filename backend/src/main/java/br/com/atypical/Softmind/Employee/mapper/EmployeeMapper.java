package br.com.atypical.Softmind.Employee.mapper;

import br.com.atypical.Softmind.Employee.dto.EmployeeCreateDto;
import br.com.atypical.Softmind.Employee.dto.EmployeeDto;
import br.com.atypical.Softmind.Employee.entities.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class EmployeeMapper {

    private final PasswordEncoder passwordEncoder;

    public static Employee toEntity(EmployeeCreateDto dto) {
        if (dto == null) return null;
        Employee employee = new Employee();
        employee.setName(dto.name());
        employee.setEmail(dto.email());
        employee.setRole(dto.role());
        employee.setSector(dto.sector());
        employee.setPermission(dto.permission()); // se for realmente campo do employee
        employee.setActive(true);
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());
        return employee;
    }


    public static EmployeeDto toDto(Employee entity) {
        if (entity == null) return null;
        return new EmployeeDto(
                entity.getId(),
                entity.getCompanyId(),
                entity.getName(),
                entity.getEmail(),
                entity.getRole(),
                entity.getSector(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.isActive()
        );
    }
}
