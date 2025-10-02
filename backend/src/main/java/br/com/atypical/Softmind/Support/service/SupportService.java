package br.com.atypical.Softmind.Support.service;

import br.com.atypical.Softmind.Support.dto.SupportCreateDto;
import br.com.atypical.Softmind.Support.entities.Support;
import br.com.atypical.Softmind.Support.mapper.SupportMapper;
import br.com.atypical.Softmind.Support.repository.SupportRepository;
import br.com.atypical.Softmind.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportService {

    private final SupportRepository repository;


    public Support create(SupportCreateDto dto) {
        Support support = SupportMapper.toEntity(dto);
        return repository.save(support);
    }

    public List<Support> listAll() {
        return repository.findAll();
    }

    public List<Support> listSupportAll() {
        return repository.findAll();
    }

    public Support update(String id, SupportCreateDto dto) {
        Support existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ponto de apoio não encontrado"));

        existing.setName(dto.name());
        existing.setDescription(dto.description());
        existing.setContactNumber(dto.contactNumber());

        return repository.save(existing);
    }

    public void delete(String id) {
        Support support = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ponto de apoio não encontrado"));
        repository.delete(support);
    }
}
