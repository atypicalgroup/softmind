package br.com.atypical.Softmind.Company.repository;

import br.com.atypical.Softmind.Company.entities.Company;
import com.mongodb.client.MongoIterable;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CompanyRepository extends MongoRepository<Company, ObjectId> {

    Optional<Company> findById(String id);
    Optional<Company> findByName(String name);
    Optional<Company> findByEmail(String email);
    Optional<Company> findByCnpj(String cnpj);

    void deleteById(String id);
}
