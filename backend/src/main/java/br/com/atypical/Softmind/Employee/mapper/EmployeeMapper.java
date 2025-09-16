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
        return  new Employee(
                null,
                dto.companyId(),
                dto.name(),
                dto.email(),
                dto.role(),
                dto.permission(),
                dto.sector(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                true
        );
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
