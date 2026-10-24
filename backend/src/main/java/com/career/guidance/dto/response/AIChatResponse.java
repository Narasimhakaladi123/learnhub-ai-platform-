package com.career.guidance.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class AIChatResponse {
    private UUID conversationId;
    private UUID messageId;
    private String role;
    private String content;
    private LocalDateTime timestamp = LocalDateTime.now();

    public AIChatResponse() {
    }

    public AIChatResponse(UUID conversationId, UUID messageId, String role, String content) {
        this.conversationId = conversationId;
        this.messageId = messageId;
        this.role = role;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public UUID getConversationId() {
        return conversationId;
    }

    public void setConversationId(UUID conversationId) {
        this.conversationId = conversationId;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
