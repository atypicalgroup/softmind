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
        "{\"text\": \"Como você avalia sua carga de trabalho?\", \"type\": \"MULTIPLE_CHOICE\", \"options\": [\"Muito Leve\", \"Leve\", \"Média\", \"Alta\", \"Muito Alta\"]}," +
        "{\"text\": \"Sua carga de trabalho afeta sua qualidade de vida?\", \"type\": \"MULTIPLE_CHOICE\", \"options\": [\"Não\", \"Raramente\", \"Às vezes\", \"Frequentemente\", \"Sempre\"]}," +
        "{\"text\": \"Você trabalha além do seu horário regular?\", \"type\": \"MULTIPLE_CHOICE\", \"options\": [\"Não\", \"Raramente\", \"Às vezes\", \"Frequentemente\", \"Sempre\"]}," +
        "{\"text\": \"Você tem apresentado sintomas como insônia, irritabilidade ou cansaço extremo?\", \"type\": \"MULTIPLE_CHOICE\", \"options\": [\"Nunca\", \"Raramente\", \"Às vezes\", \"Frequentemente\", \"Sempre\"]}," +
        "{\"text\": \"Você sente que sua saúde mental prejudica sua produtividade no trabalho?\", \"type\": \"MULTIPLE_CHOICE\", \"options\": [\"Nunca\", \"Raramente\", \"Às vezes\", \"Frequentemente\", \"Sempre\"]}," +
        "{\"text\": \"Como está o seu relacionamento com seu chefe numa escala de 1 a 5?\", \"type\": \"SCALE\", \"options\": [\"1\", \"2\", \"3\", \"4\", \"5\"]}," +
        "{\"text\": \"Como está o seu relacionamento com seus colegas de trabalho numa escala de 1 a 5?\", \"type\": \"SCALE\", \"options\": [\"1\", \"2\", \"3\", \"4\", \"5\"]}," +
        "{\"text\": \"Sinto que sou tratado(a) com respeito pelos meus colegas de trabalho.\", \"type\": \"SCALE\", \"options\": [\"1\", \"2\", \"3\", \"4\", \"5\"]}," +
        "{\"text\": \"Consigo me relacionar de forma saudável e colaborativa com minha equipe.\", \"type\": \"SCALE\", \"options\": [\"1\", \"2\", \"3\", \"4\", \"5\"]}," +
        "{\"text\": \"Tenho liberdade para expressar minhas opiniões sem medo de retaliações.\", \"type\": \"SCALE\", \"options\": [\"1\", \"2\", \"3\", \"4\", \"5\"]}," +
        "{\"text\": \"Me sinto acolhido(a) a parte do time onde trabalho.\", \"type\": \"SCALE\", \"options\": [\"1\", \"2\", \"3\", \"4\", \"5\"]}," +
        "{\"text\": \"Sinto que existe espírito de cooperação entre os colaboradores.\", \"type\": \"SCALE\", \"options\": [\"1\", \"2\", \"3\", \"4\", \"5\"]}," +
        "{\"text\": \"Recebo orientações claras e objetivas sobre minhas atividades e responsabilidades.\", \"type\": \"SCALE\", \"options\": [\"1\", \"2\", \"3\", \"4\", \"5\"]}," +
        "{\"text\": \"Sinto que posso me comunicar abertamente com minha liderança.\", \"type\": \"SCALE\", \"options\": [\"1\", \"2\", \"3\", \"4\", \"5\"]}," +
        "{\"text\": \"As informações importantes circulam de forma eficiente dentro da empresa.\", \"type\": \"SCALE\", \"options\": [\"1\", \"2\", \"3\", \"4\", \"5\"]}," +
        "{\"text\": \"Tenho clareza sobre as metas e os resultados esperados de mim.\", \"type\": \"SCALE\", \"options\": [\"1\", \"2\", \"3\", \"4\", \"5\"]}," +
        "{\"text\": \"Minha liderança demonstra interesse pelo meu bem-estar no trabalho.\", \"type\": \"SCALE\", \"options\": [\"1\", \"2\", \"3\", \"4\", \"5\"]}," +
        "{\"text\": \"Minha liderança está disponível para me ouvir quando necessário.\", \"type\": \"SCALE\", \"options\": [\"1\", \"2\", \"3\", \"4\", \"5\"]}," +
        "{\"text\": \"Me sinto confortável para reportar problemas ou dificuldades ao meu líder.\", \"type\": \"SCALE\", \"options\": [\"1\", \"2\", \"3\", \"4\", \"5\"]}," +
        "{\"text\": \"Minha liderança reconhece minhas entregas e esforços.\", \"type\": \"SCALE\", \"options\": [\"1\", \"2\", \"3\", \"4\", \"5\"]}," +
        "{\"text\": \"Existe confiança e transparência na relação com minha liderança.\", \"type\": \"SCALE\", \"options\": [\"1\", \"2\", \"3\", \"4\", \"5\"]}" +
    "]";

    // Random number generator
    private static final Random RANDOM = new Random();

    // API endpoint URLs
    private static final String API_ENDPOINT_EMPLOYEES = "http://localhost:8000/employees/responses/daily";
    private static final String API_ENDPOINT_AUTH = "http://localhost:8000/auth/login";
    private static final String[] MOOD_EMOJIS = {"😔", "😟", "😠", "😄", "😱", "🥱"};
    private static final String[] MOOD_FEELINGS = {"Sad", "Anxious", "Anger", "Happy", "Fear", "Tired"};
    private static final String API_ENDPOINT_MOOD = "http://localhost:8000/mood/daily/recommendations";

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
                    String employeeId = columns[1];
                    surveyId = columns[2]; // Assuming surveyId is the second column
                    String username = columns[0];

                    // Store employee data (we only need to keep one record per employee)
                    employeeData.put(employeeId, new String[]{surveyId, username});
                }
            }
        }

        // Define the date range (15/09/2025 to 21/09/2025)
        LocalDate startDate = LocalDate.of(2025, 10, 6);
        LocalDate endDate = LocalDate.of(2025, 10, 12);

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

                // Envio para a API de mood/daily/recommendations
                int moodIndex = RANDOM.nextInt(MOOD_EMOJIS.length);
                String moodEmoji = MOOD_EMOJIS[moodIndex];
                String moodFeeling = MOOD_FEELINGS[moodIndex];
                JSONObject moodPayload = new JSONObject();
                moodPayload.put("emoji", moodEmoji);
                moodPayload.put("feeling", moodFeeling);
                moodPayload.put("createdAt", formattedDate);
                boolean moodSuccess = sendMoodRecommendation(moodPayload.toString(), authToken);
                if (moodSuccess) {
                    System.out.println("Successfully sent mood for employee: " + employeeId + " on " + currentDate);
                } else {
                    System.err.println("Failed to send mood for employee: " + employeeId + " on " + currentDate);
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
     * Send mood recommendation to the API
     * @param moodData the JSON data containing mood information
     * @param authToken the authentication token
     * @return true if the request was successful, false otherwise
     */
    private static boolean sendMoodRecommendation(String moodData, String authToken) {
        try {
            URL url = new URL(API_ENDPOINT_MOOD);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + authToken);
            connection.setDoOutput(true);

            // Send JSON data
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = moodData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Check response code
            int responseCode = connection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                return true;
            } else {
                System.err.println("Error response code: " + responseCode);

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
            System.err.println("Error sending mood data: " + e.getMessage());
            return false;
        }
    }

    /**
     * Main method to execute the JSON HTTP request sending functionality
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            String inputFilePath = "src/main/resources/Result_34.csv";

            sendJsonSurveyData(inputFilePath);
        } catch (IOException | InterruptedException e) {
            System.err.println("Error processing data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
