package com.ai.java.service;

import com.ai.java.dto.ChatRequest;
import com.ai.java.dto.ChatResponse;

public interface AIService {
    ChatResponse chat(ChatRequest request);
}