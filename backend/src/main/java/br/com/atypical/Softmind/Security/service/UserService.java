package br.com.atypical.Softmind.Security.service;

import org.apache.commons.text.RandomStringGenerator;
import br.com.atypical.Softmind.Company.entities.Company;
import br.com.atypical.Softmind.Company.service.CompanyService;
import br.com.atypical.Softmind.Employee.dto.EmployeeCreateDto;
import br.com.atypical.Softmind.Employee.dto.EmployeeDto;
import br.com.atypical.Softmind.Employee.entities.Employee;
import br.com.atypical.Softmind.Employee.mapper.EmployeeMapper;
import br.com.atypical.Softmind.Employee.service.EmployeeService;
import br.com.atypical.Softmind.Security.dto.AdminRegisterDto;
import br.com.atypical.Softmind.Security.dto.AdminResponseDto;
import br.com.atypical.Softmind.Security.entities.User;
import br.com.atypical.Softmind.Security.helpers.EmailHelper;
import br.com.atypical.Softmind.Security.repository.UserRepository;
import br.com.atypical.Softmind.shared.enums.Permission;
import br.com.atypical.Softmind.shared.exceptions.NotFoundException;
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
    private final EmailHelper emailHelper;

    // 🔹 Cria o primeiro ADMIN da empresa (e do sistema)
    public AdminResponseDto registerAdmin(AdminRegisterDto dto) {
        // 1️⃣ Cria a empresa
        Company company = companyService.create(dto.company());

        // 2️⃣ Cria o funcionário vinculado
        Employee adminEmployee = employeeService.createAdmin(company.getId(), dto.username());

        // 3️⃣ Cria o usuário com base no funcionário
        User user = new User();
        user.setUsername(dto.username());
        user.setPermission(Permission.ADMIN);
        user.setEmployeeId(adminEmployee.getId());
        user.setEnabled(true);

        // Primeiro admin → senha manual
        boolean isFirstAdmin = userRepository.count() == 0;

        if (isFirstAdmin) {
            user.setPassword(passwordEncoder.encode(dto.password()));
            user.setMustChangePassword(false);
            System.out.println("👑 Primeiro administrador criado com senha definida manualmente.");
        } else {
            // Senha temporária + e-mail de boas-vindas
            String tempPassword = generateTempPassword();
            user.setPassword(passwordEncoder.encode(tempPassword));
            user.setMustChangePassword(true);
            emailHelper.sendWelcomeEmail(dto.username(), tempPassword);
        }

        userRepository.save(user);

        return new AdminResponseDto(
                user.getId(),
                user.getUsername(),
                user.getPermission(),
                company.getId(),
                adminEmployee.getId()
        );
    }

    // 🔹 Cria um novo EMPLOYEE e seu USER associado
    public User registerEmployee(EmployeeCreateDto dto, String companyId) {
        // 1️⃣ Cria o funcionário
        Employee employee = employeeService.createEntity(dto, companyId);

        // 2️⃣ Cria o usuário vinculado
        String tempPassword = generateTempPassword();
        User user = new User();
        user.setUsername(employee.getEmail());
        user.setPassword(passwordEncoder.encode(tempPassword));
        user.setEmployeeId(employee.getId());
        user.setPermission(Permission.valueOf(dto.permission()));
        user.setEnabled(true);
        user.setMustChangePassword(true);

        userRepository.save(user);

        // 3️⃣ Dispara o e-mail de boas-vindas
        emailHelper.sendWelcomeEmail(employee.getEmail(), tempPassword);

        return user;
    }

    // 🔹 Troca de senha (primeiro login)
    public void changePassword(String userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setMustChangePassword(false);
        userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public EmployeeDto updateEmployeeAndUser(String employeeId, EmployeeCreateDto dto, String companyId) {
        // Atualiza o employee
        Employee updatedEmployee = employeeService.updateEntity(employeeId, dto, companyId);

        // Busca o User vinculado por employeeId
        Optional<User> userOpt = userRepository.findByEmployeeId(updatedEmployee.getId());

        userOpt.ifPresent(user -> {
            // Atualiza informações do login
            user.setUsername(updatedEmployee.getEmail());
            user.setPermission(Permission.valueOf(dto.permission()));
            userRepository.save(user);
        });

        return EmployeeMapper.toDto(updatedEmployee);
    }
    public Optional<String> findEmployeeNameById(String employeeId) {
        return employeeService.findById(employeeId).map(EmployeeDto::name);
    }
    public String generateTempPassword() {
        return RandomStringGenerator.builder()
                .withinRange('0', 'z')
                .filteredBy(c -> Character.isLetterOrDigit((char) c))
                .get()
                .generate(8);
    }


    public Optional<EmployeeDto> findEmployeeById(String employeeId) {
        return employeeService.findById(employeeId);
    }

}
