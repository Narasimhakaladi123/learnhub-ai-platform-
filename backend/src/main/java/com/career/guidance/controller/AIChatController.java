package com.career.guidance.controller;

import com.career.guidance.dto.request.AIChatRequest;
import com.career.guidance.dto.response.AIChatResponse;
import com.career.guidance.dto.response.ApiResponse;
import com.career.guidance.entity.User;
import com.career.guidance.service.AICareerAssistantService;
import com.career.guidance.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@Tag(name = "AI Career Assistant", description = "RAG-augmented conversational agent for guidance, roadmap advice, and interview preparation")
public class AIChatController {

    private final AICareerAssistantService assistantService;
    private final AuthService authService;

    public AIChatController(AICareerAssistantService assistantService, AuthService authService) {
        this.assistantService = assistantService;
        this.authService = authService;
    }

    @PostMapping("/send")
    @Operation(summary = "Send a prompt to the AI career advisor and receive a contextual response")
    public ResponseEntity<ApiResponse<AIChatResponse>> sendMessage(@Valid @RequestBody AIChatRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok(assistantService.sendMessage(user, request)));
    }

    @GetMapping("/history")
    @Operation(summary = "Get the latest conversation chat history for current student")
    public ResponseEntity<ApiResponse<List<AIChatResponse>>> getActiveConversationHistory() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok(assistantService.getActiveConversationHistory(user)));
    }

    @GetMapping("/conversations/{conversationId}")
    @Operation(summary = "Get messages for a specific conversation session")
    public ResponseEntity<ApiResponse<List<AIChatResponse>>> getConversationMessages(@PathVariable UUID conversationId) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok(assistantService.getConversationMessages(user, conversationId)));
    }

    @DeleteMapping("/conversations/{conversationId}")
    @Operation(summary = "Clear a conversation thread")
    public ResponseEntity<ApiResponse<Void>> clearConversation(@PathVariable UUID conversationId) {
        User user = authService.getCurrentAuthenticatedUser();
        assistantService.clearConversation(user, conversationId);
        return ResponseEntity.ok(ApiResponse.ok("Conversation history cleared", null));
    }
}
