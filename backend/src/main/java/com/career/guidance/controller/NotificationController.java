package com.career.guidance.controller;

import com.career.guidance.dto.response.ApiResponse;
import com.career.guidance.dto.response.NotificationDto;
import com.career.guidance.entity.User;
import com.career.guidance.service.AuthService;
import com.career.guidance.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "Student system alerts, interview reminders, and achievement updates")
public class NotificationController {

    private final NotificationService notificationService;
    private final AuthService authService;

    public NotificationController(NotificationService notificationService, AuthService authService) {
        this.notificationService = notificationService;
        this.authService = authService;
    }

    @GetMapping
    @Operation(summary = "Get all notifications for authenticated user")
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getNotifications() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok(notificationService.getUserNotifications(user)));
    }

    @GetMapping("/unread-count")
    @Operation(summary = "Get count of unread notifications")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getUnreadCount() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.ok(Map.of("unreadCount", notificationService.getUnreadCount(user))));
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Mark a notification as read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable UUID id) {
        User user = authService.getCurrentAuthenticatedUser();
        notificationService.markAsRead(id, user);
        return ResponseEntity.ok(ApiResponse.ok("Notification marked as read", null));
    }
}
