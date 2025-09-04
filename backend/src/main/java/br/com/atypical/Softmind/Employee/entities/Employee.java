package br.com.atypical.Softmind.Employee.entities;


import br.com.atypical.Softmind.Company.entities.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tb_employees")
public class Employee {

    @Id
    private String id;

    private String companyId;

    private String name;
    private String email;
    private String role;
    private String sector;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    private boolean active = true;

}
