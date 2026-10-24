package com.career.guidance.controller;

import com.career.guidance.dto.response.AdminAnalyticsDto;
import com.career.guidance.dto.response.ApiResponse;
import com.career.guidance.dto.response.UserProfileResponse;
import com.career.guidance.entity.User;
import com.career.guidance.entity.enums.RoleType;
import com.career.guidance.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin & Analytics", description = "System metrics, user management, and institutional insights")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/analytics")
    @Operation(summary = "Get high-level platform analytics and cohort metrics")
    public ResponseEntity<ApiResponse<AdminAnalyticsDto>> getAnalytics() {
        return ResponseEntity.ok(ApiResponse.ok(adminService.getAnalytics()));
    }

    @GetMapping("/students")
    @Operation(summary = "Get detailed profiles for all registered students")
    public ResponseEntity<ApiResponse<List<UserProfileResponse>>> getAllStudents() {
        return ResponseEntity.ok(ApiResponse.ok(adminService.getAllStudents()));
    }

    @GetMapping("/users")
    @Operation(summary = "Get all users in the system")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.ok(adminService.getAllUsers()));
    }

    @PatchMapping("/users/{userId}/role")
    @Operation(summary = "Change role of a user")
    public ResponseEntity<ApiResponse<User>> updateUserRole(
            @PathVariable UUID userId,
            @RequestParam RoleType role) {
        return ResponseEntity.ok(ApiResponse.ok("User role updated", adminService.updateUserRole(userId, role)));
    }

    @PatchMapping("/users/{userId}/status")
    @Operation(summary = "Enable or disable a user account")
    public ResponseEntity<ApiResponse<Void>> toggleUserStatus(
            @PathVariable UUID userId,
            @RequestParam boolean enabled) {
        adminService.toggleUserStatus(userId, enabled);
        return ResponseEntity.ok(ApiResponse.ok("User status updated", null));
    }
}
