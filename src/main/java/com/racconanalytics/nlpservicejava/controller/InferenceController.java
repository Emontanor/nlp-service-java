package com.racconanalytics.nlpservicejava.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.racconanalytics.nlpservicejava.dto.InferenceRequest;
import com.racconanalytics.nlpservicejava.dto.InferenceResponse;
import com.racconanalytics.nlpservicejava.service.OllamaService;
import com.racconanalytics.nlpservicejava.service.PromptBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/inference")
@CrossOrigin(origins = "*")
public class InferenceController {

    private static final Logger logger = LoggerFactory.getLogger(InferenceController.class);

    @Autowired
    private OllamaService ollamaService;

    @Autowired
    private PromptBuilder promptBuilder;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        try {
            ollamaService.checkHealth();
            return ResponseEntity.ok("{\"status\":\"healthy\",\"ollama\":\"connected\"}");
        } catch (Exception e) {
            return ResponseEntity.status(503).body("{\"status\":\"unhealthy\",\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping
    public ResponseEntity<InferenceResponse> processInference(@RequestBody InferenceRequest request) {
        try {
            String prompt = promptBuilder.buildInferencePrompt(request.getQuery(), request.getKeywordsCount(), request.getExpandedQueriesCount());
            JsonNode ollamaResponse = ollamaService.callOllama(prompt);

            InferenceResponse response = new InferenceResponse();
            response.setOriginalQuery(request.getQuery());
            response.setKeywords(toStringList(ollamaResponse.get("keywords")));
            response.setExpandedQueries(toStringList(ollamaResponse.get("expanded_queries")));
            response.setLanguage(ollamaResponse.get("language").asText());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error processing inference request", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private List<String> toStringList(JsonNode node) {
        List<String> list = new ArrayList<>();
        if (node.isArray()) {
            for (JsonNode item : node) {
                list.add(item.asText());
            }
        }
        return list;
    }
}