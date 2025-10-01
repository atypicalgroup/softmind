package br.com.atypical.Softmind.Mood.repository;

import br.com.atypical.Softmind.Mood.entities.DailyMood;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DailyMoodRepository extends MongoRepository<DailyMood, String> {
    List<DailyMood> findByEmployeeId(String employeeId);
    List<DailyMood> findByCompanyId(String companyId);
}
