package br.com.atypical.Softmind.Company.service;

import br.com.atypical.Softmind.Company.dto.CompanyCreateDto;
import br.com.atypical.Softmind.Company.dto.CompanyDto;
import br.com.atypical.Softmind.Company.entities.Company;
import br.com.atypical.Softmind.Company.mapper.CompanyMapper;
import br.com.atypical.Softmind.Company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyDto create(CompanyCreateDto dto) {
        Company company = CompanyMapper.toEntity(dto);
        Company saved = companyRepository.save(company);;
        return CompanyMapper.toDto(saved);
    }

    public List<CompanyDto> findAll() {
        return companyRepository.findAll().stream().map(CompanyMapper::toDto).collect(Collectors.toList());
    }

    public Optional<CompanyDto> findById(String id) {
        return companyRepository.findById(id)
                .map(CompanyMapper::toDto);
    }

    public Optional<CompanyDto> findByName(String name) {
        return companyRepository.findByCnpj(name).map(CompanyMapper::toDto);
    }

    public Optional<CompanyDto> findByCnpj(String cnpj) {
        return companyRepository.findByCnpj(cnpj).map(CompanyMapper::toDto);
    }

    public Optional<CompanyDto> findByEmail(String email) {
        return companyRepository.findByEmail(email).map(CompanyMapper::toDto);
    }

    public Optional<CompanyDto> update(String id, CompanyCreateDto dto) {
        return companyRepository.findById(id).map(existing -> {
            existing.setName(dto.name());
            existing.setCnpj(dto.cnpj());
            existing.setEmail(dto.email());
            existing.setPhone(dto.phone());
            existing.setAddress(CompanyMapper.toEntity(dto).getAddress());
            existing.setUpdatedAt(LocalDateTime.now());

            Company updated = companyRepository.save(existing);
            return CompanyMapper.toDto(updated);
        });
    }

    public void delete(String id) {
        companyRepository.deleteById(id);
    }

}
