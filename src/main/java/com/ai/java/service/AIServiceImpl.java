package com.ai.java.service;

import com.ai.java.client.GeminiClient;
import com.ai.java.dto.ChatRequest;
import com.ai.java.dto.ChatResponse;
import com.ai.java.entity.ChatMessage;
import com.ai.java.repository.ChatRepository;
import com.ai.java.util.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {

    private final ChatRepository chatRepository;
    private final GeminiClient geminiClient;
    private final RateLimiter rateLimiter;

    @Override
    public ChatResponse chat(ChatRequest request) {

        String userId = request.getUserId();
        String userInput = request.getMessage();

        log.info("User: {}, Message: {}", userId, userInput);

        validateRateLimit(userId);

        saveMessage(userId, userInput, "user");

        List<ChatMessage> history = getChatHistory(userId);

        List<Map<String, Object>> contents = buildContents(history);

        String aiReply = geminiClient.callGemini(contents);

        saveMessage(userId, aiReply, "model");

        return new ChatResponse(aiReply);
    }

    private void validateRateLimit(String userId) {
        if (!rateLimiter.isAllowed(userId)) {
            throw new RuntimeException("Too many requests. Try again later.");
        }
    }

    private void saveMessage(String userId, String message, String role) {
        chatRepository.save(ChatMessage.builder()
                .userId(userId)
                .role(role)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build());
    }

    private List<ChatMessage> getChatHistory(String userId) {
        return chatRepository.findByUserIdOrderByTimestampAsc(userId);
    }

    private List<Map<String, Object>> buildContents(List<ChatMessage> history) {

        List<Map<String, Object>> contents = new ArrayList<>();

        // system prompt
        contents.add(Map.of(
                "role", "user",
                "parts", List.of(
                        Map.of("text", "You are a helpful Java backend assistant.")
                )
        ));

        for (ChatMessage msg : history) {
            contents.add(Map.of(
                    "role", msg.getRole().equals("user") ? "user" : "model",
                    "parts", List.of(Map.of("text", msg.getMessage()))
            ));
        }

        return contents;
    }
}