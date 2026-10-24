package com.career.guidance.controller;

import com.career.guidance.dto.request.SubmitAssessmentRequest;
import com.career.guidance.dto.response.ApiResponse;
import com.career.guidance.dto.response.AssessmentDto;
import com.career.guidance.dto.response.AssessmentResultDto;
import com.career.guidance.entity.User;
import com.career.guidance.service.AssessmentService;
import com.career.guidance.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/assessments")
@Tag(name = "Technical Assessments", description = "Quizzes and skill evaluations with automated scoring")
public class AssessmentController {

    private final AssessmentService assessmentService;
    private final AuthService authService;

    public AssessmentController(AssessmentService assessmentService, AuthService authService) {
        this.assessmentService = assessmentService;
        this.authService = authService;
    }

    @GetMapping
    @Operation(summary = "Get list of all technical skill assessments")
    public ResponseEntity<ApiResponse<List<AssessmentDto>>> getAllAssessments() {
        return ResponseEntity.ok(ApiResponse.ok(assessmentService.getAllAssessments()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get assessment questions by assessment ID")
    public ResponseEntity<ApiResponse<AssessmentDto>> getAssessmentById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(assessmentService.getAssessmentById(id)));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get assessment questions by slug")
    public ResponseEntity<ApiResponse<AssessmentDto>> getAssessmentBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.ok(assessmentService.getAssessmentBySlug(slug)));
    }

    @PostMapping("/submit")
    @Operation(summary = "Submit assessment answers and receive instant evaluation")
    public ResponseEntity<ApiResponse<AssessmentResultDto>> submitAssessment(@Valid @RequestBody SubmitAssessmentRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok("Assessment submitted successfully", assessmentService.submitAssessment(user, request)));
    }

    @GetMapping("/history")
    @Operation(summary = "Get user's past assessment submissions")
    public ResponseEntity<ApiResponse<List<AssessmentResultDto>>> getUserAssessmentHistory() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok(assessmentService.getUserAssessmentHistory(user)));
    }

    @GetMapping("/results/{attemptId}")
    @Operation(summary = "Get detailed breakdown of a specific assessment attempt")
    public ResponseEntity<ApiResponse<AssessmentResultDto>> getAttemptResult(@PathVariable UUID attemptId) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok(assessmentService.getAttemptResult(user, attemptId)));
    }
}
