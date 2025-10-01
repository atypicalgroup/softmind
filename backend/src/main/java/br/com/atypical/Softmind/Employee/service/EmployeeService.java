package br.com.atypical.Softmind.Employee.service;

import br.com.atypical.Softmind.Company.repository.CompanyRepository;
import br.com.atypical.Softmind.Employee.dto.EmployeeCreateDto;
import br.com.atypical.Softmind.Employee.dto.EmployeeDto;
import br.com.atypical.Softmind.Employee.entities.Employee;
import br.com.atypical.Softmind.Employee.mapper.EmployeeMapper;
import br.com.atypical.Softmind.Employee.repository.EmployeeRepository;
import br.com.atypical.Softmind.security.entities.User;
import br.com.atypical.Softmind.security.repository.UserRepository;
import br.com.atypical.Softmind.shared.enums.Permission;
import br.com.atypical.Softmind.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public Employee createAdmin(String companyId, String email) {
        Employee admin = new Employee();
        admin.setCompanyId(companyId);
        admin.setName("Administrador");
        admin.setEmail(email);
        admin.setRole("ADMIN");
        admin.setSector("Coordenação");
        return employeeRepository.save(admin);
    }


    public EmployeeDto create(EmployeeCreateDto dto, String currentCompanyId) {
        // garante que a empresa existe
        companyRepository.findById(currentCompanyId)
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada"));

        Employee employee = EmployeeMapper.toEntity(dto);
        employee.setCompanyId(currentCompanyId); // 🔹 agora o vínculo vem do contexto

        Employee savedEmployee = employeeRepository.save(employee);

        User user = new User();
        user.setUsername(savedEmployee.getEmail());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setEmployeeId(savedEmployee.getId());
        user.setPermission(Permission.valueOf(dto.permission()));
        user.setEnabled(true);

        userRepository.save(user);

        return EmployeeMapper.toDto(savedEmployee);
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

    public Optional<EmployeeDto> update(String id, EmployeeCreateDto dto, String currentCompanyId) {
        return employeeRepository.findById(id).map(existing -> {

            if (!existing.getCompanyId().equals(currentCompanyId)) {
                throw new NotFoundException("Funcionário não pertence à empresa do usuário logado");
            }

            existing.setName(dto.name());
            existing.setEmail(dto.email());
            existing.setRole(dto.role());
            existing.setSector(dto.sector());
            existing.setUpdatedAt(LocalDateTime.now());

            Employee updated = employeeRepository.save(existing);

            // 🔥 também atualiza o User vinculado (username/email, permission, etc.)
            userRepository.findByEmployeeId(id).ifPresent(user -> {
                user.setUsername(updated.getEmail());
                user.setPermission(Permission.valueOf(dto.permission()));
                userRepository.save(user);
            });

            return EmployeeMapper.toDto(updated);
        });
    }



    public void delete(String id) {
        employeeRepository.deleteById(id);
    }
}
