package br.com.atypical.Softmind.Company.entities;

import br.com.atypical.Softmind.shared.CompanyStatus;
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
    private Address address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private CompanyStatus status =  CompanyStatus.ACTIVE;

}
