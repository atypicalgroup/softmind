package br.com.atypical.Softmind.Report.service;

import br.com.atypical.Softmind.Employee.repository.EmployeeRepository;
import br.com.atypical.Softmind.Mood.entities.DailyMood;
import br.com.atypical.Softmind.Mood.repository.DailyMoodRepository;
import br.com.atypical.Softmind.Mood.dto.DailyMoodRequestDto;
import br.com.atypical.Softmind.Report.dto.DailyMoodReportDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyMoodReportService {

    private final DailyMoodRepository dailyMoodRepository;
    private final EmployeeRepository employeeRepository;

    private final Map<String, Integer> EMOJI_MAPPING = Map.of(
            "apaixonado", 100,
            "feliz", 50,
            "neutro", 0,
            "cansado", -25,
            "triste", -50,
            "raiva", -100
    );

    public DailyMoodReportDto getWeeklyReport(String companyId, LocalDate referenceDate) {
        var startOfWeek = referenceDate.with(DayOfWeek.MONDAY).atStartOfDay();
        var endOfWeek = referenceDate.with(DayOfWeek.SUNDAY).atTime(23, 59, 59);

        var moods = dailyMoodRepository.findByCompanyIdAndCreatedAtBetween(companyId, startOfWeek, endOfWeek);
        var employees = employeeRepository.findByCompanyId(companyId);

        long totalEmployees = employees.size();
        long totalParticipants = moods.stream().map(DailyMood::getEmployeeId).distinct().count();

        // Engajamento
        BigDecimal engagement = BigDecimal.valueOf(
                totalEmployees > 0 ? (double) totalParticipants / totalEmployees * 100 : 0
        ).setScale(2, RoundingMode.HALF_EVEN);

        // Score médio
        int soma = moods.stream()
                .mapToInt(m -> EMOJI_MAPPING.getOrDefault(m.getFeeling().toLowerCase(), 0))
                .sum();

        BigDecimal avg = BigDecimal.valueOf(
                moods.isEmpty() ? 0 : (double) soma / moods.size()
        ).setScale(2, RoundingMode.HALF_EVEN);

        // Agrupamento por sentimento
        Map<String, Long> feelingDistribution = moods.stream()
                .collect(Collectors.groupingBy(DailyMood::getFeeling, Collectors.counting()));

        return new DailyMoodReportDto(
                startOfWeek.toLocalDate(),
                endOfWeek.toLocalDate(),
                engagement,
                avg,
                feelingDistribution
        );
    }
}

