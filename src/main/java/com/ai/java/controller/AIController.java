package com.ai.java.controller;

import com.ai.java.dto.ChatRequest;
import com.ai.java.dto.ChatResponse;
import com.ai.java.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        return aiService.chat(request);
    }
}