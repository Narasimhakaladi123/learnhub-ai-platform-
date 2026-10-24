package com.career.guidance.service;

import com.career.guidance.dto.request.AIChatRequest;
import com.career.guidance.dto.response.AIChatResponse;
import com.career.guidance.entity.User;

import java.util.List;
import java.util.UUID;

public interface AICareerAssistantService {
    AIChatResponse sendMessage(User user, AIChatRequest request);
    List<AIChatResponse> getConversationMessages(User user, UUID conversationId);
    List<AIChatResponse> getActiveConversationHistory(User user);
    void clearConversation(User user, UUID conversationId);
}
