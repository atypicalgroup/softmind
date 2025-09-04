package br.com.atypical.Softmind.Company.controller;

import br.com.atypical.Softmind.Company.dto.CompanyCreateDto;
import br.com.atypical.Softmind.Company.dto.CompanyDto;
import br.com.atypical.Softmind.Company.service.CompanyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
@Tag(name = "Companies", description = "Endpoint para administracão da empresa cadastrada")
public class CompanyController {

    private CompanyService service;

    @PostMapping
    public ResponseEntity<CompanyDto> create(@RequestBody CompanyCreateDto dto) {
        CompanyDto created = service.create(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<CompanyDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<CompanyDto> getByCnpj(@PathVariable String cnpj) {
        return service.findByCnpj(cnpj)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<CompanyDto> getByName(@PathVariable String name) {
        return service.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CompanyDto> getByEmail(@PathVariable String email) {
        return service.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDto> update(@PathVariable String id,
                                             @RequestBody CompanyCreateDto dto) {
        return service.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
