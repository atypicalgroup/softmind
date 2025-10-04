package br.com.atypical.Softmind.Report.service;

import br.com.atypical.Softmind.Employee.entities.Employee;
import br.com.atypical.Softmind.Employee.repository.EmployeeRepository;
import br.com.atypical.Softmind.Mood.entities.DailyMood;
import br.com.atypical.Softmind.Mood.repository.DailyMoodRepository;
import br.com.atypical.Softmind.Report.dto.AdminReportDTO;
import br.com.atypical.Softmind.Report.dto.AdminReportWeekSummaryDTO;
import br.com.atypical.Softmind.Report.dto.DailyResponseDTO;
import br.com.atypical.Softmind.Report.dto.QuestionResponseDTO;
import br.com.atypical.Softmind.Report.dto.ResponseDTO;
import br.com.atypical.Softmind.Report.dto.SurveyParticipantsDTO;
import br.com.atypical.Softmind.Report.dto.SurveySummaryDTO;
import br.com.atypical.Softmind.Survey.entities.Question;
import br.com.atypical.Softmind.Survey.entities.Survey;
import br.com.atypical.Softmind.Survey.entities.SurveyResponse;
import br.com.atypical.Softmind.Survey.repository.SurveyRepository;
import br.com.atypical.Softmind.Survey.repository.SurveyResponseRepository;
import br.com.atypical.Softmind.security.entities.User;
import br.com.atypical.Softmind.shared.enums.QuestionType;
import br.com.atypical.Softmind.shared.exceptions.NotFoundException;
import br.com.atypical.Softmind.shared.utils.EmojiUtils;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final SurveyRepository surveyRepository;
    private final SurveyResponseRepository surveyResponseRepository;
    private final EmployeeRepository employeeRepository;
    private final DailyMoodRepository dailyMoodRepository;

    private final Map<String, Integer> EMOJI_MAPPING = Map.of(
            "happy", 100,
            "sad", 50,
            "tired", 0,
            "anxious", -25,
            "fear", -50
    );

    public AdminReportDTO getAdminReport(LocalDate date, User user) {
        Employee creator = employeeRepository.findById(user.getEmployeeId())
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado"));

        String companyId = creator.getCompanyId();

        var startOfWeek = Optional.ofNullable(date).orElse(LocalDate.now())
                .with(DayOfWeek.MONDAY).atStartOfDay();
        var endOfWeek = Optional.ofNullable(date).orElse(LocalDate.now())
                .with(DayOfWeek.SUNDAY).atTime(23, 59, 59);

        var startOfPreviousWeek = startOfWeek.minusWeeks(1);
        var endOfPreviousWeek = endOfWeek.minusWeeks(1);

        // Pesquisas de clima
        var surveys = surveyRepository.findByCompanyId(companyId);
        var companyEmployees = employeeRepository.findByCompanyId(companyId)
                .stream().filter(Employee::isActive).toList();

        List<SurveySummaryDTO> currentSurveySummary = surveys.stream()
                .map(s -> buildSurveySummary(s, companyEmployees, startOfWeek, endOfWeek))
                .toList();

        List<SurveySummaryDTO> prevSurveySummary = surveys.stream()
                .map(s -> buildSurveySummary(s, companyEmployees, startOfPreviousWeek, endOfPreviousWeek))
                .toList();

        AdminReportWeekSummaryDTO summary = buildAdminReportSummary(currentSurveySummary, companyEmployees);

        // DailyMood → calcula o Emocionômetro
        List<DailyMood> currentMoods = dailyMoodRepository.findByCompanyIdAndCreatedAtBetween(companyId, startOfWeek, endOfWeek);
        List<DailyMood> prevMoods = dailyMoodRepository.findByCompanyIdAndCreatedAtBetween(companyId, startOfPreviousWeek, endOfPreviousWeek);

        BigDecimal avgCurrent = calculateAverageMood(currentMoods);
        BigDecimal avgPrevious = calculateAverageMood(prevMoods);
        BigDecimal healthyPercentage = getHealthyPercentage(avgPrevious, avgCurrent);

        return AdminReportDTO.builder()
                .surveySummary(currentSurveySummary)
                .weekSummary(summary)
                .healthyPercentage(healthyPercentage)
                .startOfWeek(startOfWeek.toLocalDate())
                .endOfWeek(endOfWeek.toLocalDate())
                .build();
    }

    private BigDecimal calculateAverageMood(List<DailyMood> moods) {
        if (moods == null || moods.isEmpty()) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN);
        }
        long total = moods.size();
        long soma = moods.stream()
                .mapToInt(m -> {
                    return EMOJI_MAPPING.getOrDefault(m.getFeeling().toLowerCase(), 0);
                })
                .sum();
        return BigDecimal.valueOf((double) soma / total)
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    private static BigDecimal getHealthyPercentage(BigDecimal prev, BigDecimal curr) {
        if (prev.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return curr.subtract(prev)
                .divide(prev, 4, RoundingMode.HALF_EVEN)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    private AdminReportWeekSummaryDTO buildAdminReportSummary(List<SurveySummaryDTO> surveySummaryList, List<Employee> companyEmployees) {
        var globalQuestionResponseCounts = aggregateGlobalQuestions(surveySummaryList);
        var mostVotedResponses = findMostVotedResponses(globalQuestionResponseCounts);

        var totalResponses = surveySummaryList.stream()
                .map(SurveySummaryDTO::getParticipants)
                .mapToLong(SurveyParticipantsDTO::getTotal)
                .sum();

        return AdminReportWeekSummaryDTO.builder()
                .mostVotedResponses(mostVotedResponses)
                .overallEngagement(calculateEngagement(companyEmployees.size(), totalResponses))
                .participants(SurveyParticipantsDTO.builder()
                        .total(totalResponses)
                        .totalPerSector(Collections.emptyMap()) // anônimo
                        .build())
                .build();
    }

    private SurveySummaryDTO buildSurveySummary(Survey survey, List<Employee> companyEmployees,
                                                LocalDateTime start, LocalDateTime end) {
        var surveyResponses = surveyResponseRepository.findBySurveyIdAndAnsweredAtBetween(survey.getId(), start, end);

        Map<String, QuestionType> questionTextToType = mapQuestionTextToType(survey);
        Map<String, Map<String, Map<String, Long>>> questionDailyResponseCounts = new HashMap<>();

        for (SurveyResponse resp : surveyResponses) {
            String date = resp.getAnsweredAt().toLocalDate().format(DateTimeFormatter.ISO_DATE);
            resp.getAnswers().stream()
                    .filter(a -> questionTextToType.containsKey(a.getQuestionText()))
                    .forEach(a -> questionDailyResponseCounts
                            .computeIfAbsent(a.getQuestionText(), q -> new HashMap<>())
                            .computeIfAbsent(date, d -> new HashMap<>())
                            .merge(a.getResponse(), 1L, Long::sum));
        }

        List<QuestionResponseDTO> questionResponseDTOs = convertToQuestionResponseDTOs(questionDailyResponseCounts);
        long numberOfResponses = surveyResponses.size();

        return SurveySummaryDTO.builder()
                .surveyId(survey.getId())
                .surveyTitle(survey.getTitle())
                .questionResponses(questionResponseDTOs)
                .participants(SurveyParticipantsDTO.builder()
                        .total(numberOfResponses)
                        .totalPerSector(Collections.emptyMap()) // anônimo
                        .build())
                .engagement(calculateEngagement(companyEmployees.size(), numberOfResponses))
                .build();
    }

    private List<QuestionResponseDTO> convertToQuestionResponseDTOs(Map<String, Map<String, Map<String, Long>>> questionDailyResponseCounts) {
        return questionDailyResponseCounts.entrySet().stream()
                .map(q -> {
                    List<DailyResponseDTO> dailyResponses = q.getValue().entrySet().stream()
                            .map(d -> DailyResponseDTO.builder()
                                    .date(d.getKey())
                                    .ranking(d.getValue().entrySet().stream()
                                            .map(x -> ResponseDTO.builder().response(x.getKey()).quantity(x.getValue()).build())
                                            .sorted().toList())
                                    .build())
                            .sorted().toList();
                    return QuestionResponseDTO.builder()
                            .question(q.getKey())
                            .dailyResponses(dailyResponses)
                            .build();
                }).toList();
    }

    private Map<String, Map<String, Long>> aggregateGlobalQuestions(List<SurveySummaryDTO> surveySummaryList) {
        Map<String, Map<String, Long>> globalCounts = new HashMap<>();
        for (SurveySummaryDTO s : surveySummaryList) {
            if (s.getQuestionResponses() != null) {
                for (QuestionResponseDTO qr : s.getQuestionResponses()) {
                    for (DailyResponseDTO dr : qr.getDailyResponses()) {
                        for (ResponseDTO resp : dr.getRanking()) {
                            globalCounts
                                    .computeIfAbsent(qr.getQuestion(), q -> new HashMap<>())
                                    .merge(resp.getResponse(), resp.getQuantity(), Long::sum);
                        }
                    }
                }
            }
        }
        return globalCounts;
    }

    private Map<String, QuestionType> mapQuestionTextToType(Survey survey) {
        return survey.getQuestions().stream()
                .filter(q -> Set.of(QuestionType.MULTIPLE_CHOICE, QuestionType.EMOJI, QuestionType.SCALE).contains(q.getType()))
                .collect(Collectors.toMap(Question::getText, Question::getType));
    }

    private BigDecimal calculateEngagement(long totalEmployees, long totalResponses) {
        return BigDecimal.valueOf(totalEmployees > 0 ? (double) totalResponses / totalEmployees * 100 : 0)
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    private Map<String, Map<String, Long>> findMostVotedResponses(Map<String, Map<String, Long>> questionResponseCounts) {
        Map<String, Map<String, Long>> mostVoted = new HashMap<>();
        for (var entry : questionResponseCounts.entrySet()) {
            entry.getValue().entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .ifPresent(max -> mostVoted.put(entry.getKey(), Map.of(max.getKey(), max.getValue())));
        }
        return mostVoted;
    }

    // PDF generation (sem grandes mudanças além do uso do DailyMood no healthy)
    public byte[] generateWeeklySummaryPdf(LocalDate date, User user) throws IOException {
        Employee creator = employeeRepository.findById(user.getEmployeeId())
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado"));
        String companyId = creator.getCompanyId();

        PdfFont emojiFont = PdfFontFactory.createFont(
                ResourceUtils.getURL("classpath:DejaVuSans.ttf").toString(),
                PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED
        );

        var reportDTO = getAdminReport(date, user);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf);

            doc.add(new Paragraph("Resumo semanal").setFontSize(28).setBold());
            doc.add(new Paragraph("Evolução dos sentimentos e clima organizacional.").setFontSize(12));
            doc.add(new Paragraph(" "));

            Table dataBlocks = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .useAllAvailableWidth();
            dataBlocks.addCell(new Cell().add(new Paragraph(reportDTO.getSurveySummary().getFirst().getEngagement()
                            + "%\nEngajamento dos colaboradores"))
                    .setTextAlignment(TextAlignment.CENTER).setFontSize(24).setBackgroundColor(ColorConstants.GREEN));

            String destaque = EmojiUtils.mapDescriptionToEmoji("Feliz"); // exemplo simplificado
            dataBlocks.addCell(new Cell().add(new Paragraph(destaque + "\nSentimento da semana"))
                    .setFont(emojiFont).setFontSize(20).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(ColorConstants.YELLOW));

            doc.add(dataBlocks);
            doc.add(new Paragraph("O bem-estar emocional da equipe " + getHealthLabel(reportDTO))
                    .setFontSize(16).setBackgroundColor(ColorConstants.GREEN));

            doc.close();
            return baos.toByteArray();
        }
    }

    private String getHealthLabel(AdminReportDTO reportDTO) {
        if (reportDTO.getHealthyPercentage().compareTo(BigDecimal.ZERO) == 0) {
            return "manteve-se estável.";
        }
        return reportDTO.getHealthyPercentage().compareTo(BigDecimal.ZERO) > 0
                ? "cresceu " + reportDTO.getHealthyPercentage() + "%."
                : "diminuiu " + reportDTO.getHealthyPercentage().negate() + "%.";
    }
}
