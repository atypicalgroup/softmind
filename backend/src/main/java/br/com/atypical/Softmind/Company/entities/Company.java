package br.com.atypical.Softmind.Company.entities;

import br.com.atypical.Softmind.shared.enums.CompanyStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tb_companies")
public class Company {

    @Id
    private String id;

    private String name;
    private String cnpj;
    private String email;
    private String phone;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    private CompanyStatus status = CompanyStatus.ACTIVE;
}
