package br.com.atypical.Softmind.Security.repository;

import br.com.atypical.Softmind.Security.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmployeeId(String id);
}
