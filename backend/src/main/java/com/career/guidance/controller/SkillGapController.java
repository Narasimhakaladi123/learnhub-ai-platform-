package com.career.guidance.controller;

import com.career.guidance.dto.response.ApiResponse;
import com.career.guidance.dto.response.SkillGapDto;
import com.career.guidance.entity.User;
import com.career.guidance.service.AuthService;
import com.career.guidance.service.SkillGapAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/skill-gaps")
@Tag(name = "Skill Gap Analysis", description = "AI comparative analysis between student competencies and target job requirements")
public class SkillGapController {

    private final SkillGapAnalysisService skillGapService;
    private final AuthService authService;

    public SkillGapController(SkillGapAnalysisService skillGapService, AuthService authService) {
        this.skillGapService = skillGapService;
        this.authService = authService;
    }

    @GetMapping
    @Operation(summary = "Get skill gaps for current user's target career")
    public ResponseEntity<ApiResponse<List<SkillGapDto>>> getGapsForTargetCareer() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok(skillGapService.getSkillGapsForTargetCareer(user)));
    }

    @GetMapping("/career/{careerId}")
    @Operation(summary = "Analyze skill gaps against a specific career path")
    public ResponseEntity<ApiResponse<List<SkillGapDto>>> analyzeSkillGaps(@PathVariable UUID careerId) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok(skillGapService.analyzeSkillGaps(user, careerId)));
    }
}
