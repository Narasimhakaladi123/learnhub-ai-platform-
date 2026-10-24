package com.career.guidance.controller;

import com.career.guidance.dto.response.ApiResponse;
import com.career.guidance.dto.response.RoadmapDto;
import com.career.guidance.dto.response.RoadmapItemDto;
import com.career.guidance.entity.User;
import com.career.guidance.entity.enums.RoadmapStatus;
import com.career.guidance.service.AuthService;
import com.career.guidance.service.LearningRoadmapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/roadmaps")
@Tag(name = "Learning Roadmaps", description = "Personalized learning pathways, milestone progress, and resource links")
public class LearningRoadmapController {

    private final LearningRoadmapService roadmapService;
    private final AuthService authService;

    public LearningRoadmapController(LearningRoadmapService roadmapService, AuthService authService) {
        this.roadmapService = roadmapService;
        this.authService = authService;
    }

    @GetMapping("/active")
    @Operation(summary = "Get the student's currently active learning roadmap")
    public ResponseEntity<ApiResponse<RoadmapDto>> getActiveRoadmap() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok(roadmapService.getActiveRoadmap(user)));
    }

    @PostMapping("/generate/{careerId}")
    @Operation(summary = "Generate a personalized roadmap for a career path based on skill gaps")
    public ResponseEntity<ApiResponse<RoadmapDto>> generateRoadmap(@PathVariable UUID careerId) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok("Roadmap generated", roadmapService.generateRoadmap(user, careerId)));
    }

    @PatchMapping("/items/{itemId}/status")
    @Operation(summary = "Update progress status of a roadmap item")
    public ResponseEntity<ApiResponse<RoadmapItemDto>> updateItemStatus(
            @PathVariable UUID itemId,
            @RequestParam RoadmapStatus status) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok("Roadmap item updated", roadmapService.updateItemStatus(user, itemId, status)));
    }
}
