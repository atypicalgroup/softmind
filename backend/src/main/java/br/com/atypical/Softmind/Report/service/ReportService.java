package br.com.atypical.Softmind.Report.service;

import br.com.atypical.Softmind.Employee.entities.Employee;
import br.com.atypical.Softmind.Employee.repository.EmployeeRepository;
import br.com.atypical.Softmind.Mood.entities.DailyMood;
import br.com.atypical.Softmind.Mood.repository.DailyMoodRepository;
import br.com.atypical.Softmind.Report.dto.AdminReportDTO;
import br.com.atypical.Softmind.Report.dto.AdminReportWeekSummaryDTO;
import br.com.atypical.Softmind.Report.dto.DailyResponseDTO;
import br.com.atypical.Softmind.Report.dto.MoodSummaryDTO;
import br.com.atypical.Softmind.Report.dto.QuestionResponseDTO;
import br.com.atypical.Softmind.Report.dto.ResponseDTO;
import br.com.atypical.Softmind.Report.dto.SurveyParticipantsDTO;
import br.com.atypical.Softmind.Report.dto.SurveySummaryDTO;
import br.com.atypical.Softmind.Survey.entities.Question;
import br.com.atypical.Softmind.Survey.entities.Survey;
import br.com.atypical.Softmind.Survey.entities.SurveyParticipation;
import br.com.atypical.Softmind.Survey.entities.SurveyResponse;
import br.com.atypical.Softmind.Survey.repository.SurveyParticipationRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final SurveyRepository surveyRepository;
    private final SurveyResponseRepository surveyResponseRepository;
    private final SurveyParticipationRepository surveyParticipationRepository;
    private final EmployeeRepository employeeRepository;
    private final DailyMoodRepository dailyMoodRepository;

    private static final Map<String, Integer> EMOJI_MAPPING = new HashMap<>();
    private static final Map<String, Set<String>> FEELING_EQUIVALENTS = new HashMap<>();

    static {
        FEELING_EQUIVALENTS.put("happy", Set.of("happy", "alegre"));
        FEELING_EQUIVALENTS.put("sad", Set.of("sad", "triste"));
        FEELING_EQUIVALENTS.put("tired", Set.of("tired", "cansado"));
        FEELING_EQUIVALENTS.put("anxious", Set.of("anxious", "ansioso"));
        FEELING_EQUIVALENTS.put("fear", Set.of("fear", "medo"));
        FEELING_EQUIVALENTS.put("anger", Set.of("anger", "raiva"));

        EMOJI_MAPPING.put("happy", 2);
        EMOJI_MAPPING.put("sad", -2);
        EMOJI_MAPPING.put("tired", 0);
        EMOJI_MAPPING.put("anxious", -1);
        EMOJI_MAPPING.put("fear", -1);
        EMOJI_MAPPING.put("anger", -2);
    }

    public AdminReportDTO getAdminReport(LocalDate date, User user) {
        Employee creator = employeeRepository.findById(user.getEmployeeId())
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado"));

        String companyId = creator.getCompanyId();

        var startOfWeek = Optional.ofNullable(date).orElse(LocalDate.now())
                .with(DayOfWeek.MONDAY).atStartOfDay().minusHours(3);
        var endOfWeek = Optional.ofNullable(date).orElse(LocalDate.now())
                .with(DayOfWeek.SUNDAY).atTime(23, 59, 59).minusHours(3);

        var startOfPreviousWeek = startOfWeek.minusWeeks(1);
        var endOfPreviousWeek = endOfWeek.minusWeeks(1);

        // Pesquisas de clima
        var surveys = surveyRepository.findByCompanyId(companyId);
        var companyEmployees = employeeRepository.findByCompanyId(companyId)
                .stream().filter(Employee::isActive).toList();

        List<SurveySummaryDTO> currentSurveySummary = surveys.stream()
                .map(s -> buildSurveySummary(s, companyEmployees, startOfWeek, endOfWeek))
                .toList();

        AdminReportWeekSummaryDTO summary = buildAdminReportSummary(currentSurveySummary);

        // DailyMood → calcula o Emocionômetro
        List<DailyMood> prevMoods = dailyMoodRepository.findByCompanyIdAndCreatedAtBetween(companyId, startOfPreviousWeek, endOfPreviousWeek);
        List<DailyMood> currentMoods = dailyMoodRepository.findByCompanyIdAndCreatedAtBetween(companyId, startOfWeek, endOfWeek);

        BigDecimal avgCurrent = calculateAverageMood(currentMoods);
        BigDecimal avgPrevious = calculateAverageMood(prevMoods);
        BigDecimal healthyPercentage = getHealthyPercentage(avgPrevious, avgCurrent);

        return AdminReportDTO.builder()
                .surveySummary(currentSurveySummary)
                .weekSummary(summary)
                .previousHealthyPercentage(avgPrevious)
                .currentHealthyPercentage(avgCurrent)
                .healthyPercentage(healthyPercentage)
                .moodSummary(buildMoodSummary(currentMoods))
                .startOfWeek(startOfWeek.toLocalDate())
                .endOfWeek(endOfWeek.toLocalDate())
                .alerts(List.of("O bem-estar emocional da equipe " + getHealthLabel(avgPrevious, avgCurrent, healthyPercentage)))
                .build();
    }

    private BigDecimal calculateAverageMood(List<DailyMood> moods) {
        if (moods == null || moods.isEmpty()) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN);
        }

        long total = moods.size();
        long soma = moods.stream()
                .filter(m -> m.getFeeling() != null)
                .mapToInt(m -> EMOJI_MAPPING.getOrDefault(normalizeFeeling(m.getFeeling()).toLowerCase(), 0))
                .sum();
        return BigDecimal.valueOf((double) soma / total)
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    public MoodSummaryDTO buildMoodSummary(List<DailyMood> moods) {
        Map<String, Long> emojiCounts = getEmojiCounts(moods);

        Map<String, BigDecimal> percentages = new HashMap<>();
        for (Map.Entry<String, Long> entry : emojiCounts.entrySet()) {
            BigDecimal percent = BigDecimal.valueOf((double) entry.getValue() / moods.size() * 100)
                    .setScale(2, RoundingMode.HALF_EVEN);
            percentages.put(entry.getKey(), percent);
        }

        var mainMood = percentages.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        return MoodSummaryDTO.builder()
                .mainMoodOfWeek(mainMood)
                .moods(percentages)
                .build();
    }

    private Map<String, Long> getEmojiCounts(List<DailyMood> moods) {
        return moods.stream()
                .filter(m -> m.getFeeling() != null && !m.getFeeling().isBlank())
                .collect(Collectors.groupingBy((v) -> normalizeFeeling(v.getFeeling()), Collectors.counting()));
    }

    private static BigDecimal getHealthyPercentage(BigDecimal prev, BigDecimal curr) {
        if (prev.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal diff = curr.subtract(prev);
        BigDecimal percent = diff.abs().divide(prev.abs(), 4, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100));

        if (prev.compareTo(BigDecimal.ZERO) < 0 && curr.compareTo(BigDecimal.ZERO) < 0 && curr.compareTo(prev) < 0) {
            percent = percent.negate();
        } else if (prev.compareTo(BigDecimal.ZERO) < 0 && curr.compareTo(BigDecimal.ZERO) < 0 && curr.compareTo(prev) > 0) {
            percent = percent;
        } else {
            percent = diff.divide(prev, 4, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100));
        }

        return percent.setScale(2, RoundingMode.HALF_EVEN);
    }

    private AdminReportWeekSummaryDTO buildAdminReportSummary(List<SurveySummaryDTO> surveySummaryList) {
        var globalQuestionResponseCounts = aggregateGlobalQuestions(surveySummaryList);
        var mostVotedResponses = findMostVotedResponses(globalQuestionResponseCounts);

        BigDecimal averageEngagement = surveySummaryList.stream()
                .map(SurveySummaryDTO::getEngagement)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(Math.max(1, surveySummaryList.size())), 2, RoundingMode.HALF_EVEN);

        var totalResponses = surveySummaryList.stream()
                .map(SurveySummaryDTO::getParticipants)
                .mapToLong(SurveyParticipantsDTO::getTotal)
                .sum();

        List<String> allUniqueParticipants = surveySummaryList.stream()
                .flatMap(s -> s.getParticipants().getTotalPerSector() != null ?
                        s.getParticipants().getTotalPerSector().entrySet().stream().flatMap(e -> Stream.generate(e::getKey).limit(e.getValue()))
                        : Stream.empty())
                .toList();
        Map<String, Long> totalPerSector = allUniqueParticipants.stream()
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));

        return AdminReportWeekSummaryDTO.builder()
                .mostVotedResponses(mostVotedResponses)
                .overallEngagement(averageEngagement)
                .participants(SurveyParticipantsDTO.builder()
                        .total(totalResponses)
                        .totalPerSector(totalPerSector)
                        .build())
                .build();
    }

    private SurveySummaryDTO buildSurveySummary(Survey survey, List<Employee> companyEmployees,
                                                LocalDateTime start, LocalDateTime end) {
        var surveyResponses = surveyResponseRepository.findBySurveyIdAndAnsweredAtBetween(survey.getId(), start, end);
        var surveyParticipation = surveyParticipationRepository.findBySurveyIdAndParticipationDateBetween(survey.getId(), start, end);

        Map<String, QuestionType> questionTextToType = mapQuestionTextToType(survey);
        Map<String, Map<String, Map<String, Long>>> questionDailyResponseCounts = new HashMap<>();

        int totalEmployees = companyEmployees.size();

        List<String> uniqueParticipantsList = surveyParticipation.stream()
                .map(SurveyParticipation::getEmployeeId)
                .distinct()
                .toList();

        long uniqueParticipantsCount = uniqueParticipantsList.size();

        Map<String, Long> totalPerSector = companyEmployees.stream()
                .filter(e -> uniqueParticipantsList.contains(e.getId()))
                .collect(Collectors.groupingBy(
                        Employee::getSector,
                        Collectors.counting()
                ));

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

        return SurveySummaryDTO.builder()
                .surveyId(survey.getId())
                .surveyTitle(survey.getTitle())
                .questionResponses(questionResponseDTOs)
                .participants(SurveyParticipantsDTO.builder()
                        .total(uniqueParticipantsCount)
                        .totalPerSector(totalPerSector)
                        .build())
                .engagement(calculateSimpleEngagement(totalEmployees, uniqueParticipantsCount))
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

    private BigDecimal calculateSimpleEngagement(long totalEmployees, long uniqueRespondents) {
        return BigDecimal.valueOf(totalEmployees > 0 ? (double) uniqueRespondents / totalEmployees * 100 : 0)
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

            String destaque = EmojiUtils.mapDescriptionToEmoji("Feliz");
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
        return getHealthLabel(reportDTO.getPreviousHealthyPercentage(), reportDTO.getCurrentHealthyPercentage(), reportDTO.getHealthyPercentage());
    }

    private String getHealthLabel(BigDecimal avgPrevious, BigDecimal avgCurrent, BigDecimal healthyPercentage) {
        if (avgPrevious.compareTo(avgCurrent) == 0) {
            return "manteve-se estável.";
        }
        return avgCurrent.compareTo(avgPrevious) > 0
                ? "cresceu " + healthyPercentage + "%."
                : "diminuiu " + healthyPercentage + "%.";
    }

    public String normalizeFeeling(String feeling) {
        return FEELING_EQUIVALENTS.entrySet().stream()
                .filter(entry -> entry.getValue().contains(feeling.toLowerCase()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("unknown");
    }
}
