package br.com.atypical.Softmind.Report.service;

import br.com.atypical.Softmind.Employee.entities.Employee;
import br.com.atypical.Softmind.Employee.repository.EmployeeRepository;
import br.com.atypical.Softmind.Report.dto.AdminReportDTO;
import br.com.atypical.Softmind.Report.dto.AdminReportWeekSummaryDTO;
import br.com.atypical.Softmind.Report.dto.DailyResponseDTO;
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
import org.jetbrains.annotations.NotNull;
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

@Service
@RequiredArgsConstructor
public class ReportService {

    private static final String HEALTHY_QUESTION = "Qual o seu Emoji do dia?";
    private final Map<String, Integer> EMOJI_MAPPING = Map.of(
            "Apaixonado", 100,
            "Feliz", 50,
            "Neutro", 0,
            "Cansado", -25,
            "Triste", -50,
            "Raiva", -100
    );

    private final SurveyRepository surveyRepository;
    private final SurveyResponseRepository surveyResponseRepository;
    private final SurveyParticipationRepository surveyParticipationRepository;
    private final EmployeeRepository employeeRepository;


    public AdminReportDTO getAdminReport(LocalDate date, User user) {
        // pega o employee pelo user
        Employee creator = employeeRepository.findById(user.getEmployeeId())
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado"));

        String companyId = creator.getCompanyId();

        var startOfWeek = Optional.ofNullable(date).orElse(LocalDate.now()).with(DayOfWeek.MONDAY).atStartOfDay();
        var endOfWeek = Optional.ofNullable(date).orElse(LocalDate.now()).with(DayOfWeek.SUNDAY).atTime(23, 59, 59);

        var startOfPreviousWeek = startOfWeek.minusDays(1).with(DayOfWeek.MONDAY);
        var endOfPreviousWeek = startOfPreviousWeek.with(DayOfWeek.SUNDAY).toLocalDate().atTime(23, 59, 59);

        var surveys = surveyRepository.findByCompanyId(companyId);
        var companyEmployees = employeeRepository.findByCompanyId(companyId).stream().filter(Employee::isActive).toList();

        List<SurveySummaryDTO> surveySummaryList = surveys.stream()
                .map(survey -> buildSurveySummary(survey, companyEmployees, startOfWeek, endOfWeek))
                .toList();

        List<SurveySummaryDTO> previousWeeksurveySummaryList = surveys.stream()
                .map(survey -> buildSurveySummary(survey, companyEmployees, startOfPreviousWeek, endOfPreviousWeek))
                .toList();

        AdminReportWeekSummaryDTO summary = buildAdminReportSummary(surveySummaryList, companyEmployees);

        var previousWeekAggregatedGlobalQuestions = aggregateGlobalQuestions(previousWeeksurveySummaryList);
        BigDecimal averageOfPreviousWeek = calculateAverageHealthy(previousWeekAggregatedGlobalQuestions);

        var weekAggregatedGlobalQuestions = aggregateGlobalQuestions(surveySummaryList);
        BigDecimal averageOfWeek = calculateAverageHealthy(weekAggregatedGlobalQuestions);

        BigDecimal healthyPercentage = getHealthyPercentage(averageOfPreviousWeek, averageOfWeek);

        return AdminReportDTO.builder()
                .surveySummary(surveySummaryList)
                .weekSummary(summary)
                .healthyPercentage(healthyPercentage)
                .startOfWeek(startOfWeek.toLocalDate())
                .endOfWeek(endOfWeek.toLocalDate())
                .build();
    }


    @NotNull
    private static BigDecimal getHealthyPercentage(BigDecimal averageOfPreviousWeek, BigDecimal averageOfWeek) {
        BigDecimal healthyPercentage;
        if (averageOfPreviousWeek.compareTo(BigDecimal.ZERO) == 0) {
            healthyPercentage = BigDecimal.ZERO;
        } else {
            healthyPercentage = averageOfWeek.subtract(averageOfPreviousWeek)
                    .divide(averageOfPreviousWeek, 4, RoundingMode.HALF_EVEN)
                    .multiply(averageOfWeek.compareTo(averageOfPreviousWeek) >= 0 ? BigDecimal.valueOf(100) : BigDecimal.valueOf(-100))
                    .setScale(2, RoundingMode.HALF_EVEN);
        }
        return healthyPercentage;
    }

    private BigDecimal calculateAverageHealthy(Map<String, Map<String, Long>> pw) {
        long totalPW = pw.get(HEALTHY_QUESTION).values().stream().mapToLong(l -> l).sum();
        long somaPW = pw.get(HEALTHY_QUESTION).entrySet().stream()
                .mapToInt(e -> {
                    return Math.toIntExact(EMOJI_MAPPING.getOrDefault(e.getKey(), 0) * e.getValue());
                })
                .sum();

        return BigDecimal.valueOf(totalPW > 0 ? (double) somaPW / totalPW : 0).setScale(2, RoundingMode.HALF_EVEN);
    }

    private AdminReportWeekSummaryDTO buildAdminReportSummary(List<SurveySummaryDTO> surveySummaryList, List<Employee> companyEmployees) {
        var globalQuestionResponseCounts = aggregateGlobalQuestions(surveySummaryList);
        var mostVotedResponses = findMostVotedResponses(globalQuestionResponseCounts);

        var totalUniqueParticipants = surveySummaryList.stream()
                .map(SurveySummaryDTO::getParticipants)
                .mapToLong(SurveyParticipantsDTO::getTotal)
                .sum();

        Map<String, Long> totalParticipantsBySector = aggregateParticipantsBySector(surveySummaryList);

        return AdminReportWeekSummaryDTO.builder()
                .mostVotedResponses(mostVotedResponses)
                .overallEngagement(calculateEngagement(companyEmployees.size(), totalUniqueParticipants))
                .participants(SurveyParticipantsDTO.builder()
                        .total(totalUniqueParticipants)
                        .totalPerSector(totalParticipantsBySector)
                        .build())
                .build();
    }

    private Map<String, Long> aggregateParticipantsBySector(List<SurveySummaryDTO> surveySummaryList) {
        Map<String, Long> aggregatedParticipantsBySector = new HashMap<>();

        for (SurveySummaryDTO surveySummary : surveySummaryList) {
            SurveyParticipantsDTO participants = surveySummary.getParticipants();
            if (participants != null && participants.getTotalPerSector() != null) {
                for (Map.Entry<String, Long> entry : participants.getTotalPerSector().entrySet()) {
                    String sector = entry.getKey();
                    Long count = entry.getValue();
                    aggregatedParticipantsBySector.merge(sector, count, Long::sum);
                }
            }
        }

        return aggregatedParticipantsBySector;
    }

    private SurveySummaryDTO buildSurveySummary(Survey survey, List<Employee> companyEmployees, LocalDateTime startOfWeek, LocalDateTime endOfWeek) {

        List<SurveyParticipation> surveyParticipants = surveyParticipationRepository.findBySurveyIdAndParticipationDateBetween(survey.getId(), startOfWeek, endOfWeek);
        var surveyResponses = surveyResponseRepository.findBySurveyIdAndAnsweredAtBetween(survey.getId(), startOfWeek, endOfWeek);

        Map<String, QuestionType> questionTextToType = mapQuestionTextToType(survey);

        Map<String, Map<String, Map<String, Long>>> questionDailyResponseCounts = new HashMap<>();

        for (SurveyResponse surveyResponse : surveyResponses) {
            String date = surveyResponse.getAnsweredAt().toLocalDate().format(DateTimeFormatter.ISO_DATE);
            surveyResponse.getAnswers().stream()
                    .filter(answer -> questionTextToType.containsKey(answer.getQuestionText()))
                    .forEach(answer -> {
                        String questionText = answer.getQuestionText();
                        String response = answer.getResponse();
                        questionDailyResponseCounts
                                .computeIfAbsent(questionText, q -> new HashMap<>())
                                .computeIfAbsent(date, d -> new HashMap<>())
                                .merge(response, 1L, Long::sum);
                    });
        }

        List<QuestionResponseDTO> questionResponseDTOs = convertToQuestionResponseDTOs(questionDailyResponseCounts);

        List<String> participantEmployeeIds = surveyParticipants.stream()
                .map(SurveyParticipation::getEmployeeId)
                .distinct()
                .toList();

        long numberOfParticipants = participantEmployeeIds.size();

        Map<String, Long> participantsBySector = mapParticipantsBySector(participantEmployeeIds, companyEmployees);

        BigDecimal engagement = calculateEngagement(companyEmployees.size(), numberOfParticipants);

        return SurveySummaryDTO.builder()
                .surveyId(survey.getId())
                .surveyTitle(survey.getTitle())
                .questionResponses(questionResponseDTOs)
                .participants(SurveyParticipantsDTO.builder()
                        .total(numberOfParticipants)
                        .totalPerSector(participantsBySector)
                        .build())
                .engagement(engagement)
                .build();
    }

    private List<QuestionResponseDTO> convertToQuestionResponseDTOs(Map<String, Map<String, Map<String, Long>>> questionDailyResponseCounts) {
        return questionDailyResponseCounts.entrySet().stream()
                .map(questionEntry -> {
                    String questionText = questionEntry.getKey();
                    List<DailyResponseDTO> dailyResponses = questionEntry.getValue().entrySet().stream()
                            .map(dateEntry -> DailyResponseDTO.builder()
                                    .date(dateEntry.getKey())
                                    .ranking(dateEntry.getValue().entrySet().stream().map(x -> ResponseDTO.builder().response(x.getKey()).quantity(x.getValue()).build()).sorted().toList())
                                    .build())
                            .sorted()
                            .toList();

                    return QuestionResponseDTO.builder()
                            .question(questionText)
                            .dailyResponses(dailyResponses)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private Map<String, Long> mapParticipantsBySector(List<String> participantEmployeeIds, List<Employee> companyEmployees) {

        Set<String> allCompanySectors = companyEmployees.stream()
                .map(Employee::getSector)
                .filter(sector -> sector != null && !sector.isEmpty())
                .collect(Collectors.toSet());

        Map<String, String> employeeSectors = companyEmployees.stream()
                .collect(Collectors.toMap(Employee::getId, Employee::getSector));

        Map<String, Long> participantsBySector = participantEmployeeIds.stream()
                .map(employeeSectors::get)
                .filter(sector -> sector != null && !sector.isEmpty())
                .collect(Collectors.groupingBy(
                        sector -> sector,
                        Collectors.counting()
                ));

        for (String sector : allCompanySectors) {
            participantsBySector.putIfAbsent(sector, 0L);
        }

        return participantsBySector;
    }

    private Map<String, Map<String, Long>> aggregateGlobalQuestions(List<SurveySummaryDTO> surveySummaryList) {
        Map<String, Map<String, Long>> globalCounts = new HashMap<>();

        for (SurveySummaryDTO surveySummaryDTO : surveySummaryList) {
            if (surveySummaryDTO.getQuestionResponses() != null) {
                for (QuestionResponseDTO questionResponse : surveySummaryDTO.getQuestionResponses()) {
                    String questionText = questionResponse.getQuestion();

                    if (questionResponse.getDailyResponses() != null) {
                        for (DailyResponseDTO dailyResponse : questionResponse.getDailyResponses()) {
                            if (dailyResponse.getRanking() != null) {
                                for (ResponseDTO responseEntry : dailyResponse.getRanking()) {

                                    globalCounts
                                            .computeIfAbsent(questionText, q -> new HashMap<>())
                                            .merge(responseEntry.getResponse(), responseEntry.getQuantity(), Long::sum);
                                }
                            }
                        }
                    }
                }
            }
        }

        return globalCounts;
    }

    private Map<String, QuestionType> mapQuestionTextToType(Survey survey) {
        return survey.getQuestions().stream()
                .filter(question -> Set.of(QuestionType.MULTIPLE_CHOICE, QuestionType.EMOJI, QuestionType.SCALE).contains(question.getType()))
                .collect(Collectors.toMap(Question::getText, Question::getType));
    }

    private BigDecimal calculateEngagement(long numberOfEmployeesFromCompany, long uniqueParticipants) {
        return BigDecimal.valueOf(numberOfEmployeesFromCompany > 0 ? (double) uniqueParticipants / numberOfEmployeesFromCompany * 100.0 : 0)
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    private Map<String, Map<String, Long>> findMostVotedResponses(Map<String, Map<String, Long>> questionResponseCounts) {
        Map<String, Map<String, Long>> mostVotedResponses = new HashMap<>();

        for (Map.Entry<String, Map<String, Long>> questionEntry : questionResponseCounts.entrySet()) {
            String questionText = questionEntry.getKey();
            Map<String, Long> responseCounts = questionEntry.getValue();

            if (!responseCounts.isEmpty()) {
                responseCounts.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .ifPresent(mostVotedResponse -> mostVotedResponses.put(questionText, Map.of(mostVotedResponse.getKey(), mostVotedResponse.getValue())));
            }
        }

        return mostVotedResponses;
    }

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

            // Título
            doc.add(new Paragraph("Resumo semanal")
                    .setFontSize(28)
                    .setBold());

            doc.add(new Paragraph("Nesta tela temos o resumo semanal da evolução dos sentimentos dos nossos colegas para um melhor controle de atividades.")
                    .setFontSize(12));
            doc.add(new Paragraph(" "));

            // Emocionômetro
            Table emometer = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1, 1, 1, 1}))
                    .useAllAvailableWidth();
            emometer.addHeaderCell(new Cell(1, 7)
                    .add(new Paragraph("Emocionômetro"))
                    .setTextAlignment(TextAlignment.CENTER));

            // Cabeçalho com datas
            for (DailyResponseDTO daily : reportDTO.getSurveySummary()
                    .getFirst().getQuestionResponses().getFirst().getDailyResponses()) {
                emometer.addCell(new Cell()
                        .add(new Paragraph(daily.getDate().substring(5)))
                        .setTextAlignment(TextAlignment.CENTER));
            }

            // Emojis por dia
            for (DailyResponseDTO daily : extractHealthyQuestion(reportDTO)) {
                String emoji = daily.getRanking().isEmpty() ? "" :
                        EmojiUtils.mapDescriptionToEmoji(daily.getRanking().getFirst().getResponse());
                emometer.addCell(new Cell()
                        .add(new Paragraph(emoji).setFont(emojiFont).setFontSize(20))
                        .setTextAlignment(TextAlignment.CENTER));
            }

            doc.add(emometer);
            doc.add(new Paragraph(" "));

            // Blocos de dados
            Table dataBlocks = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .useAllAvailableWidth();

            dataBlocks.addCell(new Cell().add(new Paragraph("Dados"))
                    .setTextAlignment(TextAlignment.CENTER).setBold().setFontSize(18).setBackgroundColor(ColorConstants.GREEN));

            dataBlocks.addCell(new Cell().add(new Paragraph("Dados"))
                    .setTextAlignment(TextAlignment.CENTER).setBold().setFontSize(18).setBackgroundColor(ColorConstants.YELLOW));

            dataBlocks.addCell(new Cell().add(new Paragraph(reportDTO.getSurveySummary().getFirst().getEngagement()
                            + "%\nPercentual de engajamento dos nossos colegas"))
                    .setTextAlignment(TextAlignment.CENTER).setFontSize(24).setBackgroundColor(ColorConstants.GREEN));

            String destaque = EmojiUtils.mapDescriptionToEmoji(
                    reportDTO.getWeekSummary().getMostVotedResponses().get(HEALTHY_QUESTION)
                            .entrySet().stream().findFirst().get().getKey()
            );

            dataBlocks.addCell(new Cell().add(new Paragraph(destaque + "\nSentimento e destaque nesta semana"))
                    .setFont(emojiFont).setFontSize(20).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(ColorConstants.YELLOW));

            doc.add(dataBlocks);
            doc.add(new Paragraph(" "));

            // Resumo final
            doc.add(new Paragraph("O bem-estar emocional da nossa equipe " + getHealthLabel(reportDTO))
                    .setFontSize(16)
                    .setBackgroundColor(ColorConstants.GREEN));

            doc.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF do resumo semanal", e);
        }
    }


    private List<DailyResponseDTO> extractHealthyQuestion(AdminReportDTO reportDTO) {
        return reportDTO.getSurveySummary().getFirst()
                .getQuestionResponses().stream()
                .filter(q -> q.getQuestion().equalsIgnoreCase(HEALTHY_QUESTION))
                .findFirst()
                .orElse(new QuestionResponseDTO())
                .getDailyResponses();
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