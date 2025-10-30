package br.com.atypical.Softmind.Report.controller;

import br.com.atypical.Softmind.Report.dto.AdminReportDTO;
import br.com.atypical.Softmind.Report.service.ReportService;
import br.com.atypical.Softmind.Security.entities.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminReportController {

    private final ReportService reportService;

    @Operation(
            summary = "Resumo semanal de bem-estar",
            description = "Retorna o relatório agregado semanal da empresa do admin autenticado",
            tags = "Administração"
    )
    @GetMapping
    public ResponseEntity<AdminReportDTO> getReport(@RequestParam(required = false) LocalDate date,
                                                    @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(reportService.getAdminReport(date, user));
    }

    @Operation(
            summary = "Exportar relatório semanal em PDF",
            description = "Gera e retorna o relatório semanal em PDF para a empresa do admin autenticado",
            tags = "Administração"

    )
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> reportPDF(@RequestParam(required = false) LocalDate date,
                                            @AuthenticationPrincipal User user) throws IOException {
        byte[] pdfBytes = reportService.generateWeeklySummaryPdf(date, user);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=relatorio-semanal.pdf")
                .body(pdfBytes);
    }
    @Operation(
            summary = "Resumo Geral de bem-estar",
            description = "Retorna o relatório geral da empresa do admin autenticado",
            tags = "Administração"
    )
    @GetMapping("/admin")
    public ResponseEntity<AdminReportDTO> getGlobalReport(@AuthenticationPrincipal User user) {
        var report = reportService.getGlobalAdminReport(user);
        return ResponseEntity.ok(report);
    }

}

