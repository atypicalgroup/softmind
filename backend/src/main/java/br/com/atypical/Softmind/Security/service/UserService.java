package br.com.atypical.Softmind.security.service;

import br.com.atypical.Softmind.Company.entities.Company;
import br.com.atypical.Softmind.Company.service.CompanyService;
import br.com.atypical.Softmind.Employee.dto.EmployeeDto;
import br.com.atypical.Softmind.Employee.entities.Employee;
import br.com.atypical.Softmind.Employee.mapper.EmployeeMapper;
import br.com.atypical.Softmind.Employee.service.EmployeeService;
import br.com.atypical.Softmind.security.dto.AdminRegisterDto;
import br.com.atypical.Softmind.security.dto.AdminResponseDto;
import br.com.atypical.Softmind.shared.enums.Permission;
import br.com.atypical.Softmind.security.entities.User;
import br.com.atypical.Softmind.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;

    public User register(String username, String password, String companyId, String employeeId, Permission permission) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmployeeId(employeeId);
        user.setPermission(permission);
        user.setEnabled(true);
        user.setMustChangePassword(false);
        return userRepository.save(user);
    }

    public AdminResponseDto registerAdmin(AdminRegisterDto dto) {
        Company company = companyService.create(dto.company());

        Employee adminEmployee = employeeService.createAdmin(company.getId(), dto.username());

        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setPermission(Permission.ADMIN);
        user.setEmployeeId(adminEmployee.getId());

        userRepository.save(user);

        return new AdminResponseDto(
                user.getId(),
                user.getUsername(),
                user.getPermission(),
                company.getId(),
                adminEmployee.getId()
        );
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    // ✅ Novo método para buscar o nome do funcionário pelo employeeId
    public Optional<String> findEmployeeNameById(String employeeId) {
        return employeeService.findById(employeeId).map(EmployeeDto::name);
    }


}
