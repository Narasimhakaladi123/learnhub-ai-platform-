package com.career.guidance.controller;

import com.career.guidance.dto.response.ApiResponse;
import com.career.guidance.dto.response.ProjectDto;
import com.career.guidance.entity.User;
import com.career.guidance.service.AuthService;
import com.career.guidance.service.ProjectRecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Recommended Projects", description = "Portfolio project recommendations tailored to bridge skill gaps")
public class ProjectRecommendationController {

    private final ProjectRecommendationService projectService;
    private final AuthService authService;

    public ProjectRecommendationController(ProjectRecommendationService projectService, AuthService authService) {
        this.projectService = projectService;
        this.authService = authService;
    }

    @GetMapping("/recommended")
    @Operation(summary = "Get personalized portfolio project recommendations for current student")
    public ResponseEntity<ApiResponse<List<ProjectDto>>> getRecommendedProjects() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok(projectService.getRecommendedProjects(user)));
    }

    @GetMapping("/career/{slug}")
    @Operation(summary = "Get standard portfolio projects by career path slug")
    public ResponseEntity<ApiResponse<List<ProjectDto>>> getProjectsByCareerSlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.ok(projectService.getProjectsByCareerSlug(slug)));
    }
}
