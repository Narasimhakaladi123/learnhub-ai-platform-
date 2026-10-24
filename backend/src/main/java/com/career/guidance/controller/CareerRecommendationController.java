package com.career.guidance.controller;

import com.career.guidance.dto.response.ApiResponse;
import com.career.guidance.dto.response.CareerRecommendationDto;
import com.career.guidance.entity.User;
import com.career.guidance.service.AuthService;
import com.career.guidance.service.CareerRecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/recommendations")
@Tag(name = "AI Career Recommendations", description = "Personalized AI-powered career matches based on profile, skills, and quiz scores")
public class CareerRecommendationController {

    private final CareerRecommendationService recommendationService;
    private final AuthService authService;

    public CareerRecommendationController(CareerRecommendationService recommendationService, AuthService authService) {
        this.recommendationService = recommendationService;
        this.authService = authService;
    }

    @GetMapping
    @Operation(summary = "Get saved career recommendations for current student")
    public ResponseEntity<ApiResponse<List<CareerRecommendationDto>>> getRecommendations() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok(recommendationService.getRecommendationsForUser(user)));
    }

    @PostMapping("/generate")
    @Operation(summary = "Run AI evaluation to generate new career recommendations")
    public ResponseEntity<ApiResponse<List<CareerRecommendationDto>>> generateRecommendations() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok("Recommendations generated successfully", recommendationService.generateRecommendations(user)));
    }

    @PostMapping("/select/{careerId}")
    @Operation(summary = "Set selected career as student's primary target career")
    public ResponseEntity<ApiResponse<CareerRecommendationDto>> setTargetCareer(@PathVariable UUID careerId) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok("Target career updated", recommendationService.setTargetCareer(user, careerId)));
    }
}
