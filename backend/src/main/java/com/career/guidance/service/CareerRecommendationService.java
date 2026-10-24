package com.career.guidance.service;

import com.career.guidance.dto.response.CareerRecommendationDto;
import com.career.guidance.entity.User;

import java.util.List;
import java.util.UUID;

public interface CareerRecommendationService {
    List<CareerRecommendationDto> generateRecommendations(User user);
    List<CareerRecommendationDto> getRecommendationsForUser(User user);
    CareerRecommendationDto setTargetCareer(User user, UUID careerId);
}
