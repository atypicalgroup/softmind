package br.com.atypical.Softmind.Support.repository;

import br.com.atypical.Softmind.Support.entities.Support;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SupportRepository extends MongoRepository<Support, String> {
}
