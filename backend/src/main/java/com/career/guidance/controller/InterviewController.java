package com.career.guidance.controller;

import com.career.guidance.dto.request.EvaluateInterviewAnswerRequest;
import com.career.guidance.dto.request.GenerateInterviewRequest;
import com.career.guidance.dto.response.ApiResponse;
import com.career.guidance.dto.response.InterviewEvaluationDto;
import com.career.guidance.dto.response.InterviewQuestionDto;
import com.career.guidance.entity.User;
import com.career.guidance.service.AuthService;
import com.career.guidance.service.InterviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/interviews")
@Tag(name = "AI Mock Interview", description = "Simulated technical interviews with real-time AI evaluation and feedback")
public class InterviewController {

    private final InterviewService interviewService;
    private final AuthService authService;

    public InterviewController(InterviewService interviewService, AuthService authService) {
        this.interviewService = interviewService;
        this.authService = authService;
    }

    @PostMapping("/start")
    @Operation(summary = "Start a new AI technical interview session")
    public ResponseEntity<ApiResponse<List<InterviewQuestionDto>>> startSession(@Valid @RequestBody GenerateInterviewRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        List<InterviewQuestionDto> questions = interviewService.startInterviewSession(user, request);
        return ResponseEntity.ok(ApiResponse.ok("Interview session initiated", questions));
    }

    @PostMapping("/evaluate")
    @Operation(summary = "Submit student response and receive instant AI evaluation")
    public ResponseEntity<ApiResponse<InterviewEvaluationDto>> evaluateAnswer(@Valid @RequestBody EvaluateInterviewAnswerRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        InterviewEvaluationDto evaluation = interviewService.evaluateAnswer(user, request);
        return ResponseEntity.ok(ApiResponse.ok("Response evaluated", evaluation));
    }

    @GetMapping("/sessions/{sessionId}/questions")
    @Operation(summary = "Get all questions and evaluations for a session")
    public ResponseEntity<ApiResponse<List<InterviewQuestionDto>>> getSessionQuestions(@PathVariable UUID sessionId) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok(interviewService.getSessionQuestions(user, sessionId)));
    }
}
