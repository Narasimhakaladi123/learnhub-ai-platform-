package com.career.guidance.controller;

import com.career.guidance.dto.request.*;
import com.career.guidance.dto.response.ApiResponse;
import com.career.guidance.dto.response.DashboardSummaryDto;
import com.career.guidance.dto.response.UserProfileResponse;
import com.career.guidance.entity.User;
import com.career.guidance.service.AuthService;
import com.career.guidance.service.StudentProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/profile")
@Tag(name = "Student Profile", description = "Endpoints for managing student academic background, skills, and portfolio")
public class StudentProfileController {

    private final StudentProfileService profileService;
    private final AuthService authService;

    public StudentProfileController(StudentProfileService profileService, AuthService authService) {
        this.profileService = profileService;
        this.authService = authService;
    }

    @GetMapping
    @Operation(summary = "Get current student profile details")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok(profileService.getProfile(user)));
    }

    @PutMapping
    @Operation(summary = "Update personal and academic bio details")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok("Profile updated successfully", profileService.updateProfile(user, request)));
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Get aggregated student career dashboard metrics")
    public ResponseEntity<ApiResponse<DashboardSummaryDto>> getDashboardSummary() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok(profileService.getDashboardSummary(user)));
    }

    @PostMapping("/skills")
    @Operation(summary = "Add a verified skill to student profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> addSkill(@Valid @RequestBody AddSkillRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok("Skill added successfully", profileService.addSkill(user, request)));
    }

    @DeleteMapping("/skills/{skillId}")
    @Operation(summary = "Remove a skill from profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> removeSkill(@PathVariable UUID skillId) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok("Skill removed", profileService.removeSkill(user, skillId)));
    }

    @PostMapping("/education")
    @Operation(summary = "Add education history")
    public ResponseEntity<ApiResponse<UserProfileResponse>> addEducation(@Valid @RequestBody AddEducationRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok("Education added", profileService.addEducation(user, request)));
    }

    @PostMapping("/experience")
    @Operation(summary = "Add work experience entry")
    public ResponseEntity<ApiResponse<UserProfileResponse>> addExperience(@Valid @RequestBody AddExperienceRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok("Experience added", profileService.addExperience(user, request)));
    }

    @PostMapping("/internships")
    @Operation(summary = "Add internship record")
    public ResponseEntity<ApiResponse<UserProfileResponse>> addInternship(@Valid @RequestBody AddInternshipRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok("Internship added", profileService.addInternship(user, request)));
    }

    @PostMapping("/projects")
    @Operation(summary = "Add student portfolio project")
    public ResponseEntity<ApiResponse<UserProfileResponse>> addProject(@Valid @RequestBody AddProjectRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok("Project added", profileService.addProject(user, request)));
    }

    @PostMapping("/certifications")
    @Operation(summary = "Add professional certification")
    public ResponseEntity<ApiResponse<UserProfileResponse>> addCertification(@Valid @RequestBody AddCertificationRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok("Certification added", profileService.addCertification(user, request)));
    }

    @PutMapping("/interests")
    @Operation(summary = "Update technical career interests")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateInterests(@Valid @RequestBody UpdateInterestsRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok("Interests updated", profileService.updateInterests(user, request)));
    }
}
