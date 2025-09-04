package br.com.atypical.Softmind.Employee.service;

import br.com.atypical.Softmind.Company.repository.CompanyRepository;
import br.com.atypical.Softmind.Employee.dto.EmployeeCreateDto;
import br.com.atypical.Softmind.Employee.dto.EmployeeDto;
import br.com.atypical.Softmind.Employee.entities.Employee;
import br.com.atypical.Softmind.Employee.mapper.EmployeeMapper;
import br.com.atypical.Softmind.Employee.repository.EmployeeRepository;
import br.com.atypical.Softmind.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;

    public EmployeeDto create(EmployeeCreateDto dto) {

        if (dto.companyId() == null || companyRepository.findById(dto.companyId()).isEmpty()) {
            throw new NotFoundException("Empresa não encontrada para o ID: " + dto.companyId());
        }

        Employee employee = EmployeeMapper.toEntity(dto);
        Employee saved = employeeRepository.save(employee);
        return EmployeeMapper.toDto(saved);
    }

    public List<EmployeeDto> findAll() {
        return employeeRepository.findAll()
                .stream()
                .map(EmployeeMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<EmployeeDto> findById(String id) {
        return employeeRepository.findById(id).map(EmployeeMapper::toDto);
    }

    public List<EmployeeDto> findByCompanyId(String companyId) {
        return employeeRepository.findByCompanyId(companyId)
                .stream()
                .map(EmployeeMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<EmployeeDto> findByEmail(String email) {
        return employeeRepository.findByEmail(email).map(EmployeeMapper::toDto);
    }

    public Optional<EmployeeDto> update(String id, EmployeeCreateDto dto) {
        return employeeRepository.findById(id).map(existing -> {

            if (dto.companyId() == null || companyRepository.findById(dto.companyId()).isEmpty()) {
                throw new NotFoundException("Empresa não encontrada para o ID: " + dto.companyId());
            }

            existing.setName(dto.name());
            existing.setEmail(dto.email());
            existing.setRole(dto.role());
            existing.setSector(dto.sector());
            existing.setCompanyId(dto.companyId());
            existing.setUpdatedAt(LocalDateTime.now());
            Employee updated = employeeRepository.save(existing);
            return EmployeeMapper.toDto(updated);
        });
    }

    public void delete(String id) {
        employeeRepository.deleteById(id);
    }
}
