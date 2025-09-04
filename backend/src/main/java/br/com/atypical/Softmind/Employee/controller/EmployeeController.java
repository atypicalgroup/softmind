package br.com.atypical.Softmind.Employee.controller;

import br.com.atypical.Softmind.Employee.dto.EmployeeCreateDto;
import br.com.atypical.Softmind.Employee.dto.EmployeeDto;
import br.com.atypical.Softmind.Employee.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> create(@RequestBody EmployeeCreateDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<EmployeeDto>> getByCompany(@PathVariable String companyId) {
        return ResponseEntity.ok(service.findByCompanyId(companyId));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<EmployeeDto> getByEmail(@PathVariable String email) {
        return service.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> update(@PathVariable String id, @RequestBody EmployeeCreateDto dto) {
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
