package com.career.guidance.dto.response;

import java.util.List;
import java.util.Map;

public class DashboardSummaryDto {
    private int readinessPercentage;
    private String targetCareer;
    private List<CareerRecommendationDto> topRecommendations;
    private Map<String, Integer> skillProgress; // skillName -> percentage (e.g. Java -> 90)
    private List<SkillGapDto> skillGaps;
    private RoadmapDto activeRoadmap;
    private List<AssessmentResultDto> recentAssessments;
    private List<ProjectDto> recommendedProjects;
    private List<RoadmapItemDto> upcomingLearningTasks;
    private String recentAiAdvice;

    public DashboardSummaryDto() {
    }

    // Getters and Setters
    public int getReadinessPercentage() {
        return readinessPercentage;
    }

    public void setReadinessPercentage(int readinessPercentage) {
        this.readinessPercentage = readinessPercentage;
    }

    public String getTargetCareer() {
        return targetCareer;
    }

    public void setTargetCareer(String targetCareer) {
        this.targetCareer = targetCareer;
    }

    public List<CareerRecommendationDto> getTopRecommendations() {
        return topRecommendations;
    }

    public void setTopRecommendations(List<CareerRecommendationDto> topRecommendations) {
        this.topRecommendations = topRecommendations;
    }

    public Map<String, Integer> getSkillProgress() {
        return skillProgress;
    }

    public void setSkillProgress(Map<String, Integer> skillProgress) {
        this.skillProgress = skillProgress;
    }

    public List<SkillGapDto> getSkillGaps() {
        return skillGaps;
    }

    public void setSkillGaps(List<SkillGapDto> skillGaps) {
        this.skillGaps = skillGaps;
    }

    public RoadmapDto getActiveRoadmap() {
        return activeRoadmap;
    }

    public void setActiveRoadmap(RoadmapDto activeRoadmap) {
        this.activeRoadmap = activeRoadmap;
    }

    public List<AssessmentResultDto> getRecentAssessments() {
        return recentAssessments;
    }

    public void setRecentAssessments(List<AssessmentResultDto> recentAssessments) {
        this.recentAssessments = recentAssessments;
    }

    public List<ProjectDto> getRecommendedProjects() {
        return recommendedProjects;
    }

    public void setRecommendedProjects(List<ProjectDto> recommendedProjects) {
        this.recommendedProjects = recommendedProjects;
    }

    public List<RoadmapItemDto> getUpcomingLearningTasks() {
        return upcomingLearningTasks;
    }

    public void setUpcomingLearningTasks(List<RoadmapItemDto> upcomingLearningTasks) {
        this.upcomingLearningTasks = upcomingLearningTasks;
    }

    public String getRecentAiAdvice() {
        return recentAiAdvice;
    }

    public void setRecentAiAdvice(String recentAiAdvice) {
        this.recentAiAdvice = recentAiAdvice;
    }
}
