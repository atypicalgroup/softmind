package br.com.atypical.Softmind.Survey.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "gs_tb_surveys")
public class Survey {

    @Id
    private String id;
    private String companyId;
    private String title;
    private String description;

    private List<Question> questions;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active;

}
