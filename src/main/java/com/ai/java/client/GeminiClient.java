package com.ai.java.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class GeminiClient {

    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    public String callGemini(List<Map<String, Object>> contents) {

        Map<String, Object> request = Map.of("contents", contents);

        try {
            Map response = webClient.post()
                    .uri("/v1beta/models/gemini-3-flash-preview:generateContent")
                    .header("x-goog-api-key", apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            log.info("Gemini Response: {}", response);

            return extractText(response);

        } catch (Exception e) {
            log.error("Gemini API Error", e);
            throw new RuntimeException("AI service unavailable");
        }
    }

    private String extractText(Map response) {
        List<Map> candidates = (List<Map>) response.get("candidates");
        Map content = (Map) candidates.get(0).get("content");
        List<Map> parts = (List<Map>) content.get("parts");

        return (String) parts.get(0).get("text");
    }
}