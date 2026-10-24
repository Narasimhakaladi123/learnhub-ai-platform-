package com.career.guidance.dto.response;

import java.util.Map;

public class AdminAnalyticsDto {
    private long totalStudents;
    private long totalCounselors;
    private long totalAdmins;
    private long totalAssessmentsTaken;
    private long totalRoadmapsCreated;
    private long totalResumesAnalyzed;
    private double averageReadinessScore;
    private Map<String, Long> topTargetCareers;
    private Map<String, Long> mostCommonSkillGaps;

    public AdminAnalyticsDto() {
    }

    // Getters and Setters
    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public long getTotalCounselors() {
        return totalCounselors;
    }

    public void setTotalCounselors(long totalCounselors) {
        this.totalCounselors = totalCounselors;
    }

    public long getTotalAdmins() {
        return totalAdmins;
    }

    public void setTotalAdmins(long totalAdmins) {
        this.totalAdmins = totalAdmins;
    }

    public long getTotalAssessmentsTaken() {
        return totalAssessmentsTaken;
    }

    public void setTotalAssessmentsTaken(long totalAssessmentsTaken) {
        this.totalAssessmentsTaken = totalAssessmentsTaken;
    }

    public long getTotalRoadmapsCreated() {
        return totalRoadmapsCreated;
    }

    public void setTotalRoadmapsCreated(long totalRoadmapsCreated) {
        this.totalRoadmapsCreated = totalRoadmapsCreated;
    }

    public long getTotalResumesAnalyzed() {
        return totalResumesAnalyzed;
    }

    public void setTotalResumesAnalyzed(long totalResumesAnalyzed) {
        this.totalResumesAnalyzed = totalResumesAnalyzed;
    }

    public double getAverageReadinessScore() {
        return averageReadinessScore;
    }

    public void setAverageReadinessScore(double averageReadinessScore) {
        this.averageReadinessScore = averageReadinessScore;
    }

    public Map<String, Long> getTopTargetCareers() {
        return topTargetCareers;
    }

    public void setTopTargetCareers(Map<String, Long> topTargetCareers) {
        this.topTargetCareers = topTargetCareers;
    }

    public Map<String, Long> getMostCommonSkillGaps() {
        return mostCommonSkillGaps;
    }

    public void setMostCommonSkillGaps(Map<String, Long> mostCommonSkillGaps) {
        this.mostCommonSkillGaps = mostCommonSkillGaps;
    }
}
