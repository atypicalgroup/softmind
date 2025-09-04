package br.com.atypical.Softmind.Employee.service;

import br.com.atypical.Softmind.Employee.dto.EmployeeCreateDto;
import br.com.atypical.Softmind.Employee.dto.EmployeeDto;
import br.com.atypical.Softmind.Employee.entities.Employee;
import br.com.atypical.Softmind.Employee.mapper.EmployeeMapper;
import br.com.atypical.Softmind.Employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public EmployeeDto create(EmployeeCreateDto dto) {
        Employee employee = EmployeeMapper.toEntity(dto);
        Employee saved = repository.save(employee);
        return EmployeeMapper.toDto(saved);
    }

    public List<EmployeeDto> findAll() {
        return repository.findAll()
                .stream()
                .map(EmployeeMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<EmployeeDto> findById(String id) {
        return repository.findById(id).map(EmployeeMapper::toDto);
    }

    public List<EmployeeDto> findByCompanyId(String companyId) {
        return repository.findByCompanyId(companyId)
                .stream()
                .map(EmployeeMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<EmployeeDto> findByEmail(String email) {
        return repository.findByEmail(email).map(EmployeeMapper::toDto);
    }

    public Optional<EmployeeDto> update(String id, EmployeeCreateDto dto) {
        return repository.findById(id).map(existing -> {
            existing.setName(dto.name());
            existing.setEmail(dto.email());
            existing.setRole(dto.role());
            existing.setSector(dto.sector());
            existing.setCompanyId(dto.companyId());
            existing.setUpdatedAt(LocalDateTime.now());
            Employee updated = repository.save(existing);
            return EmployeeMapper.toDto(updated);
        });
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
