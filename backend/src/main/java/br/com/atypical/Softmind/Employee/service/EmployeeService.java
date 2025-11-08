package br.com.atypical.Softmind.Employee.service;

import br.com.atypical.Softmind.Company.repository.CompanyRepository;
import br.com.atypical.Softmind.Employee.dto.EmployeeCreateDto;
import br.com.atypical.Softmind.Employee.dto.EmployeeDto;
import br.com.atypical.Softmind.Employee.entities.Employee;
import br.com.atypical.Softmind.Employee.mapper.EmployeeMapper;
import br.com.atypical.Softmind.Employee.repository.EmployeeRepository;
import br.com.atypical.Softmind.Security.service.UserService;
import br.com.atypical.Softmind.shared.enums.Permission;
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

    public Employee createAdmin(String companyId, String email) {
        Employee admin = new Employee();
        admin.setCompanyId(companyId);
        admin.setName("Administrador");
        admin.setEmail(email);
        admin.setRole("ADMIN");
        admin.setSector("Coordenação");
        return employeeRepository.save(admin);
    }

    // usado internamente pelo UserService
    public Employee createEntity(EmployeeCreateDto dto, String companyId) {
        companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada"));
        Employee employee = EmployeeMapper.toEntity(dto);
        employee.setCompanyId(companyId);
        return employeeRepository.save(employee);
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

    public Employee updateEntity(String id, EmployeeCreateDto dto, String currentCompanyId) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado"));

        if (!employee.getCompanyId().equals(currentCompanyId)) {
            throw new NotFoundException("Funcionário não pertence à empresa do usuário logado");
        }

        employee.setName(dto.name());
        employee.setEmail(dto.email());
        employee.setRole(dto.role());
        employee.setSector(dto.sector());
        employee.setUpdatedAt(LocalDateTime.now());

        return employeeRepository.save(employee);
    }


    public void delete(String id) {
        employeeRepository.deleteById(id);
    }
}
