package br.com.atypical.Softmind.Suggestion.repository;

import br.com.atypical.Softmind.Suggestion.entities.Tour;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TourRepository extends MongoRepository<Tour,String> {
    List<Tour> findByNameTour(String nameTour);

}
