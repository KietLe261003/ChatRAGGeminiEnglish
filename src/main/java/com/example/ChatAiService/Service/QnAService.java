package com.example.ChatAiService.Service;

import com.example.ChatAiService.Dto.GeminiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Map;

@Service
public class QnAService {
    // Access to APIKey and URL [Gemini]
    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final WebClient webClient;

    public QnAService(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    public GeminiResponse getAnswer(String question) {
        // Construct the request payload
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", question)
                        })
                });

        // Make API Call
        String jsonResponse = webClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            // Use ObjectMapper to convert the JSON string to a GeminiResponse object
            return objectMapper.readValue(jsonResponse, GeminiResponse.class);
        } catch (IOException e) {
            // Handle the exception appropriately (e.g., log the error, throw a custom exception)
            System.err.println("Error parsing JSON response: " + e.getMessage());
            return null; // Or throw an exception, depending on your error handling strategy
        }
    }
}
