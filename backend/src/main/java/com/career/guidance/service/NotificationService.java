package com.career.guidance.service;

import com.career.guidance.dto.response.NotificationDto;
import com.career.guidance.entity.User;
import com.career.guidance.entity.enums.NotificationType;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    void createNotification(User user, String title, String message, NotificationType type);
    List<NotificationDto> getUserNotifications(User user);
    void markAsRead(UUID notificationId, User user);
    long getUnreadCount(User user);
}
