package br.com.atypical.Softmind.Company.service;

import br.com.atypical.Softmind.Company.dto.CompanyCreateDto;
import br.com.atypical.Softmind.Company.dto.CompanyDto;
import br.com.atypical.Softmind.Company.entities.Company;
import br.com.atypical.Softmind.Company.mapper.CompanyMapper;
import br.com.atypical.Softmind.Company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public Company create(CompanyCreateDto dto) {
        Company company = new Company();
        company.setName(dto.name());
        company.setCnpj(dto.cnpj());
        company.setEmail(dto.email());
        company.setPhone(dto.phone());
        return companyRepository.save(company);
    }

//    public Optional<CompanyDto> update(String id, CompanyCreateDto dto) {
//        return companyRepository.findById(id).map(existing -> {
//            existing.setName(dto.name());
//            existing.setCnpj(dto.cnpj());
//            existing.setEmail(dto.email());
//            existing.setPhone(dto.phone());
//            existing.setUpdatedAt(LocalDateTime.now());
//
//            Company updated = companyRepository.save(existing);
//            return CompanyMapper.toDto(updated);
//        });
//    }

    public void delete(String id) {
        companyRepository.deleteById(id);
    }

}
