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
import br.com.atypical.Softmind.shared.enums.QuestionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final SurveyRepository surveyRepository;
    private final SurveyResponseRepository surveyResponseRepository;
    private final SurveyParticipationRepository surveyParticipationRepository;
    private final EmployeeRepository employeeRepository;

    public AdminReportDTO getAdminReport(String companyId) {
        var startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
        var endOfWeek = LocalDate.now().with(DayOfWeek.SUNDAY).atTime(23, 59, 59);

        var surveys = surveyRepository.findByCompanyId(companyId);
        var companyEmployees = employeeRepository.findByCompanyId(companyId).stream().filter(Employee::isActive).toList();

        List<SurveySummaryDTO> surveySummaryList = surveys.stream()
                .map(survey -> buildSurveySummary(survey, companyEmployees, startOfWeek, endOfWeek))
                .toList();

        AdminReportWeekSummaryDTO summary = buildAdminReportSummary(surveySummaryList, companyEmployees);

        return AdminReportDTO.builder()
                .surveySummary(surveySummaryList)
                .weekSummary(summary)
                .startOfWeek(startOfWeek.toLocalDate())
                .endOfWeek(endOfWeek.toLocalDate())
                .build();
    }

    private AdminReportWeekSummaryDTO buildAdminReportSummary(List<SurveySummaryDTO> surveySummaryList, List<Employee> companyEmployees) {
        var globalQuestionResponseCounts = aggregateGlobalQuestionResponseCounts(surveySummaryList);
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

        // Combine participant counts by sector from all surveys
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

    private Map<String, Map<String, Long>> aggregateGlobalQuestionResponseCounts(List<SurveySummaryDTO> surveySummaryList) {
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

    private Map<String, String> findMostVotedResponses(Map<String, Map<String, Long>> questionResponseCounts) {
        Map<String, String> mostVotedResponses = new HashMap<>();

        for (Map.Entry<String, Map<String, Long>> questionEntry : questionResponseCounts.entrySet()) {
            String questionText = questionEntry.getKey();
            Map<String, Long> responseCounts = questionEntry.getValue();

            if (!responseCounts.isEmpty()) {
                responseCounts.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey).ifPresent(mostVotedResponse -> mostVotedResponses.put(questionText, mostVotedResponse));
            }
        }

        return mostVotedResponses;
    }
}
