package com.career.guidance.service;

import com.career.guidance.dto.response.AdminAnalyticsDto;
import com.career.guidance.dto.response.UserProfileResponse;
import com.career.guidance.entity.User;
import com.career.guidance.entity.enums.RoleType;

import java.util.List;
import java.util.UUID;

public interface AdminService {
    AdminAnalyticsDto getAnalytics();
    List<UserProfileResponse> getAllStudents();
    List<User> getAllUsers();
    User updateUserRole(UUID userId, RoleType newRole);
    void toggleUserStatus(UUID userId, boolean enabled);
}
