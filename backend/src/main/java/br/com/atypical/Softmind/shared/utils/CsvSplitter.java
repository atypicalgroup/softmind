package br.com.atypical.Softmind.shared.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class to read CSV and send survey data as JSON via HTTP
 */
public class CsvSplitter {

    // Questions and options for the survey
    private static final String QUESTIONS_JSON = "[" +
        "{ \"text\":\"Qual o seu Emoji do dia?\", \"type\":\"EMOJI\", \"options\":[ \"\\uD83D\\uDE00\", \"\\uD83D\\uDE10\", \"\\uD83D\\uDE22\", \"\\uD83D\\uDE21\", \"\\uD83D\\uDE0D\", \"\\uD83D\\uDE34\" ] }, " +
        "{ \"text\":\"Qual o seu sentimento de hoje?\", \"type\":\"TEXT\" }, " +
        "{ \"text\":\"Como você diria que a sua empresa está?\", \"type\":\"MULTIPLE_CHOICE\", \"options\":[ \"Muito boa\", \"Boa\", \"Regular\", \"Ruim\", \"Muito ruim\" ] }, " +
        "{ \"text\":\"Qual o maior desafio que sua empresa enfrenta hoje?\", \"type\":\"TEXT\" }, " +
        "{ \"text\":\"Como você classificaria o clima organizacional da sua empresa?\", \"type\":\"SCALE\", \"options\":[ \"1 - Muito ruim\", \"2 - Ruim\", \"3 - Regular\", \"4 - Bom\", \"5 - Excelente\" ] }, " +
        "{ \"text\":\"Como você avalia a comunicação interna da sua empresa?\", \"type\":\"MULTIPLE_CHOICE\", \"options\":[ \"Muito eficiente\", \"Eficiente\", \"Razoável\", \"Ineficiente\", \"Muito ineficiente\" ] }, " +
        "{ \"text\":\"Qual o nível de motivação dos colaboradores na sua empresa?\", \"type\":\"EMOJI\", \"options\":[ \"\\uD83D\\uDE00 Muito motivados\", \"\\uD83D\\uDE42 Motivados\", \"\\uD83D\\uDE10 Indiferentes\", \"\\uD83D\\uDE41 Pouco motivados\", \"\\uD83D\\uDE21 Nada motivados\" ] }, " +
        "{ \"text\":\"Como você avalia o suporte que a liderança dá aos funcionários?\", \"type\":\"SCALE\", \"options\":[ \"1 - Muito ruim\", \"2 - Ruim\", \"3 - Regular\", \"4 - Bom\", \"5 - Excelente\" ] }, " +
        "{ \"text\":\"Como você enxerga as oportunidades de crescimento profissional na sua empresa?\", \"type\":\"MULTIPLE_CHOICE\", \"options\":[ \"Muitas oportunidades\", \"Algumas oportunidades\", \"Poucas oportunidades\", \"Nenhuma oportunidade\" ] }, " +
        "{ \"text\":\"Qual o grau de satisfação geral dos colaboradores com a empresa?\", \"type\":\"EMOJI\", \"options\":[ \"\\uD83D\\uDE01 Muito satisfeitos\", \"\\uD83D\\uDE42 Satisfeitos\", \"\\uD83D\\uDE10 Neutros\", \"\\uD83D\\uDE41 Insatisfeitos\", \"\\uD83D\\uDE21 Muito insatisfeitos\" ] }" +
    "]";

    private static final String[] TEXT_RESPONSES = {
        "Estou muito animado hoje!",
        "Me sinto um pouco cansado, mas produtivo.",
        "Dia difícil, muitos desafios.",
        "Motivado com os novos projetos.",
        "Preocupado com os prazos.",
        "Esperançoso com as mudanças recentes.",
        "Reflexivo sobre as metas da equipe.",
        "Focado nas entregas da semana.",
        "Ansioso com as reuniões de hoje.",
        "Satisfeito com os resultados alcançados."
    };

    private static final String[] CHALLENGE_RESPONSES = {
        "Comunicação interna precisa melhorar.",
        "Alta rotatividade de funcionários.",
        "Processos internos burocráticos.",
        "Falta de clareza nas metas e objetivos.",
        "Limitações orçamentárias.",
        "Adaptação às novas tecnologias.",
        "Concorrência acirrada no mercado.",
        "Dificuldade em atrair novos talentos.",
        "Equilibrar qualidade e velocidade nas entregas.",
        "Manter a cultura organizacional com o crescimento."
    };

    // Random number generator
    private static final Random RANDOM = new Random();

    // API endpoint URLs
    private static final String API_ENDPOINT_EMPLOYEES = "http://localhost:8000/employees/{employeeId}/68c898b0bde1421af5dcf4c9/responses";
    private static final String API_ENDPOINT_AUTH = "http://localhost:8000/auth/login";

    // Map to cache authentication tokens by username
    private static final Map<String, String> authTokenCache = new HashMap<>();

    // Delay between requests in milliseconds
    private static final int REQUEST_DELAY_MS = 500;

    /**
     * Reads a CSV file and sends survey data as JSON via HTTP requests
     * @param inputFilePath path to the source CSV file
     * @throws IOException if there's an issue reading files or sending requests
     */
    public static void sendJsonSurveyData(String inputFilePath) throws IOException, InterruptedException {
        // Parse questions
        JSONArray questionsArray = new JSONArray(QUESTIONS_JSON);

        // Map to store data for each employee ID
        Map<String, String[]> employeeData = new HashMap<>();
        String surveyId = "";

        // Read the input CSV file
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            // Skip header line
            String header = reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] columns = line.split(",");
                if (columns.length >= 3) {
                    String employeeId = columns[0];
                    surveyId = columns[1]; // Assuming surveyId is the second column
                    String username = columns[2];

                    // Store employee data (we only need to keep one record per employee)
                    employeeData.put(employeeId, new String[]{surveyId, username});
                }
            }
        }

        // Define the date range (15/09/2025 to 21/09/2025)
        LocalDate startDate = LocalDate.of(2025, 9, 22);
        LocalDate endDate = LocalDate.of(2025, 9, 28);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        System.out.println("Starting to send survey responses...");
        System.out.println("Total employees to process: " + employeeData.size());
        System.out.println("Sending responses for each day from " + startDate + " to " + endDate);

        // For each employee, generate and send a response for each day in the range
        for (Map.Entry<String, String[]> entry : employeeData.entrySet()) {
            String employeeId = entry.getKey();
            String[] employeeInfo = entry.getValue();
            String employeeSurveyId = employeeInfo[0];
            String username = employeeInfo[1];

            System.out.println("Processing employee: " + employeeId + " (" + username + ")");

            // Get authentication token once for this employee
            String authToken = getAuthToken(username);
            if (authToken == null) {
                System.err.println("Failed to get authentication token for user: " + username);
                failCount.addAndGet((int) (ChronoUnit.DAYS.between(startDate, endDate) + 1)); // Count all days as failed
                continue;
            }

            // For each day in the range
            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                // Generate random time for this day
                LocalTime randomTime = LocalTime.of(
                    RANDOM.nextInt(24),
                    RANDOM.nextInt(60),
                    RANDOM.nextInt(60)
                );

                LocalDateTime participationDateTime = LocalDateTime.of(currentDate, randomTime);
                String formattedDate = participationDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

                // Create JSON object for this employee's survey response for this day
                JSONObject surveyResponse = new JSONObject();
                surveyResponse.put("surveyId", employeeSurveyId);
                surveyResponse.put("employeeId", employeeId);
                surveyResponse.put("participationDate", formattedDate);

                // Create answers array
                JSONArray answersArray = new JSONArray();

                // Generate responses for each question
                for (int i = 0; i < questionsArray.length(); i++) {
                    JSONObject question = questionsArray.getJSONObject(i);
                    String questionText = question.getString("text");
                    String questionType = question.getString("type");
                    String response = generateResponse(question, questionType);

                    // Create answer object
                    JSONObject answer = new JSONObject();
                    answer.put("questionText", questionText);
                    answer.put("response", response);

                    // Add to answers array
                    answersArray.put(answer);
                }

                // Add answers array to survey response
                surveyResponse.put("answers", answersArray);

                // Send HTTP request with JSON payload
                boolean success = sendHttpRequest(surveyResponse.toString(4), employeeId, authToken);
                if (success) {
                    System.out.println("Successfully sent data for employee: " + employeeId + " on " + currentDate);
                    successCount.incrementAndGet();
                } else {
                    System.err.println("Failed to send data for employee: " + employeeId + " on " + currentDate);
                    failCount.incrementAndGet();
                }

                // Add a small delay to avoid overwhelming the server
                Thread.sleep(REQUEST_DELAY_MS);

                // Move to next day
                currentDate = currentDate.plusDays(1);
            }
        }

        System.out.println("JSON request sending complete.");
        System.out.println("Successful requests: " + successCount.get());
        System.out.println("Failed requests: " + failCount.get());
    }

    /**
     * Send HTTP POST request with JSON data
     * @param jsonData the JSON data to send
     * @param employeeId the ID of the employee
     * @param authToken the authentication token
     * @return true if the request was successful, false otherwise
     */
    private static boolean sendHttpRequest(String jsonData, String employeeId, String authToken) {
        try {

            // Create connection with employeeId in URL
            String requestUrl = API_ENDPOINT_EMPLOYEES.replace("{employeeId}", employeeId);
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + authToken);
            connection.setDoOutput(true);

            // Send JSON data
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Check response code
            int responseCode = connection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                return true;
            } else {
                System.err.println("Error response code: " + responseCode + " for employee ID: " + employeeId);

                // Read error message if available
                if (connection.getErrorStream() != null) {
                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        System.err.println("Error response: " + response);
                    }
                }

                return false;
            }
        } catch (Exception e) {
            System.err.println("Error sending HTTP request for employee ID " + employeeId + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Generate a random response based on question type and options
     * @param question the question object
     * @param questionType the type of question
     * @return a random response
     */
    private static String generateResponse(JSONObject question, String questionType) {
        switch (questionType) {
            case "EMOJI":
                JSONArray emojiOptions = question.getJSONArray("options");
                return emojiOptions.getString(RANDOM.nextInt(emojiOptions.length()));

            case "MULTIPLE_CHOICE":
            case "SCALE":
                JSONArray options = question.getJSONArray("options");
                return options.getString(RANDOM.nextInt(options.length()));

            case "TEXT":
                if (question.getString("text").contains("sentimento")) {
                    return TEXT_RESPONSES[RANDOM.nextInt(TEXT_RESPONSES.length)];
                } else if (question.getString("text").contains("desafio")) {
                    return CHALLENGE_RESPONSES[RANDOM.nextInt(CHALLENGE_RESPONSES.length)];
                } else {
                    return TEXT_RESPONSES[RANDOM.nextInt(TEXT_RESPONSES.length)];
                }

            default:
                return "No response";
        }
    }

    /**
     * Gets an authentication token for a username
     * @param username the username to authenticate
     * @return the authentication token or null if authentication failed
     */
    private static String getAuthToken(String username) {
        // Check if we already have a cached token for this username
        if (authTokenCache.containsKey(username)) {
            return authTokenCache.get(username);
        }

        try {
            // Create authentication request
            JSONObject authRequest = new JSONObject();
            authRequest.put("username", username);
            authRequest.put("password", "123");
            // Note: In a real-world scenario, you'd also need a password here

            // Send authentication request
            URL url = new URL(API_ENDPOINT_AUTH);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            // Send JSON data
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = authRequest.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Read response
            int responseCode = connection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                // Read response body to get token
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    // Parse JSON response to get token
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    String token = jsonResponse.getString("token");

                    // Cache the token
                    authTokenCache.put(username, token);

                    System.out.println("Successfully authenticated user: " + username);
                    return token;
                }
            } else {
                System.err.println("Authentication failed for user: " + username + ", Status code: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error during authentication for user: " + username + ", Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Main method to execute the JSON HTTP request sending functionality
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            String inputFilePath = "src/main/resources/Result_94.csv";

            sendJsonSurveyData(inputFilePath);
        } catch (IOException | InterruptedException e) {
            System.err.println("Error processing data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
