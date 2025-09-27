package br.com.atypical.Softmind.Tour.repository;

import br.com.atypical.Softmind.Tour.entities.Tour;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TourRepository extends MongoRepository<Tour,String> {
    List<Tour> findByNameTour(String nameTour);

}
