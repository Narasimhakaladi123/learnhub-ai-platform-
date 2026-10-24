package com.career.guidance.service;

import com.career.guidance.dto.response.ProjectDto;
import com.career.guidance.entity.User;

import java.util.List;

public interface ProjectRecommendationService {
    List<ProjectDto> getRecommendedProjects(User user);
    List<ProjectDto> getProjectsByCareerSlug(String careerSlug);
}
