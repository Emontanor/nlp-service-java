package com.racconanalytics.nlpservicejava.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class OllamaService {

    @Value("${ollama.api.url:http://localhost:11434/api/generate}")
    private String ollamaApiUrl;

    @Value("${ollama.model:deepseek-r1}")
    private String model;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void checkHealth() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:11434/api/tags"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Ollama service unavailable (status: " + response.statusCode() + ")");
        }
    }

    public JsonNode callOllama(String prompt) throws Exception {
        // Use ObjectMapper to properly escape JSON strings
        com.fasterxml.jackson.databind.node.ObjectNode requestJson = objectMapper.createObjectNode();
        requestJson.put("model", model);
        requestJson.put("prompt", prompt);
        requestJson.put("stream", false);
        
        String requestBody = objectMapper.writeValueAsString(requestJson);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ollamaApiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Ollama error: " + response.body());
        }

        JsonNode jsonResponse = objectMapper.readTree(response.body());
        String rawResponse = jsonResponse.get("response").asText().trim();

        // Parse JSON from response
        int start = rawResponse.indexOf("{");
        int end = rawResponse.lastIndexOf("}");
        if (start == -1 || end == -1) {
            throw new RuntimeException("Invalid response format: " + rawResponse);
        }
        String cleanJson = rawResponse.substring(start, end + 1);
        return objectMapper.readTree(cleanJson);
    }
}