package com.career.guidance.service;

import com.career.guidance.dto.response.RoadmapDto;
import com.career.guidance.dto.response.RoadmapItemDto;
import com.career.guidance.entity.User;
import com.career.guidance.entity.enums.RoadmapStatus;

import java.util.UUID;

public interface LearningRoadmapService {
    RoadmapDto generateRoadmap(User user, UUID careerId);
    RoadmapDto getActiveRoadmap(User user);
    RoadmapItemDto updateItemStatus(User user, UUID itemId, RoadmapStatus status);
}
