package br.com.atypical.Softmind.Security.repository;

import br.com.atypical.Softmind.Security.entities.PasswordResetToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface PasswordResetRepository extends MongoRepository<PasswordResetToken, String> {
    Optional<PasswordResetToken> findByEmailAndTokenAndUsedFalse(String email, String token);
}
