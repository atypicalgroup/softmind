package br.com.atypical.Softmind.security.service;

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

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;


    public User register(String username, String password, String companyId, String employeeId, Permission permission) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setCompanyId(companyId);
        user.setEmployeeId(employeeId);
        user.setPermission(permission);
        user.setEnabled(true);
        user.setMustChangePassword(false);
        return repository.save(user);
    }

//    public User registerEmployee(String username, String password, String companyId, String employeeId) {
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(passwordEncoder.encode(password));
//        user.setCompanyId(companyId);
//        user.setEmployeeId(employeeId);
//        user.setRole(Role.EMPLOYEE);
//        user.setEnabled(true);
//        user.setMustChangePassword(true);
//        return repository.save(user);
//    }
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public User save(User user) {
        return repository.save(user);
    }
}
