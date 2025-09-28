package br.com.atypical.Softmind.Report.controller;

import br.com.atypical.Softmind.Report.dto.AdminReportDTO;
import br.com.atypical.Softmind.Report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@Tag(name = "Report", description = "Relatórios e Análises")
@PreAuthorize("hasRole('ADMIN')")
public class AdminReportController {

    private final ReportService service;

    @Operation(
            summary = "Lista todas as empresas",
            description = "Retorna a lista completa de empresas cadastradas."
    )
    @GetMapping
    public ResponseEntity<AdminReportDTO> report(@RequestParam String companyId,
                                                 @RequestParam(required = false) LocalDate date) {
        return ResponseEntity.ok(service.getAdminReport(companyId, date));
    }

    @Operation(
            summary = "Lista todas as empresas",
            description = "Retorna a lista completa de empresas cadastradas."
    )
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> reportPDF(@RequestParam String companyId,
                                            @RequestParam(required = false) LocalDate date) throws IOException {
        return ResponseEntity.ok(service.generateWeeklySummaryPdf(companyId, date));
    }
}
