package com.career.guidance.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public class AIChatRequest {

    private UUID conversationId;

    @NotBlank(message = "Message content is required")
    private String message;

    public AIChatRequest() {
    }

    public AIChatRequest(UUID conversationId, String message) {
        this.conversationId = conversationId;
        this.message = message;
    }

    // Getters and Setters
    public UUID getConversationId() {
        return conversationId;
    }

    public void setConversationId(UUID conversationId) {
        this.conversationId = conversationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
