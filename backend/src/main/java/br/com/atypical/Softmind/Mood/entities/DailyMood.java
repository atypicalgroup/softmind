package br.com.atypical.Softmind.Mood.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "gs_tb_daily_moods")
public class DailyMood {

    @Id
    private String id;

    private String employeeId;
    private String companyId;
    private String emoji;
    private String feeling;
    private LocalDateTime createdAt = LocalDateTime.now();
}
