package com.career.guidance.dto.ai;

import java.util.List;

public class CareerRecommendationResponse {
    private String career;
    private int compatibilityScore;
    private List<String> matchingSkills;
    private List<String> skillGaps;
    private String explanation;
    private List<String> nextSteps;
    private List<String> recommendedProjects;
    private List<String> recommendedCertifications;

    public CareerRecommendationResponse() {
    }

    public CareerRecommendationResponse(String career, int compatibilityScore, List<String> matchingSkills, List<String> skillGaps, String explanation, List<String> nextSteps) {
        this.career = career;
        this.compatibilityScore = compatibilityScore;
        this.matchingSkills = matchingSkills;
        this.skillGaps = skillGaps;
        this.explanation = explanation;
        this.nextSteps = nextSteps;
    }

    // Getters and Setters
    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public int getCompatibilityScore() {
        return compatibilityScore;
    }

    public void setCompatibilityScore(int compatibilityScore) {
        this.compatibilityScore = compatibilityScore;
    }

    public List<String> getMatchingSkills() {
        return matchingSkills;
    }

    public void setMatchingSkills(List<String> matchingSkills) {
        this.matchingSkills = matchingSkills;
    }

    public List<String> getSkillGaps() {
        return skillGaps;
    }

    public void setSkillGaps(List<String> skillGaps) {
        this.skillGaps = skillGaps;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public List<String> getNextSteps() {
        return nextSteps;
    }

    public void setNextSteps(List<String> nextSteps) {
        this.nextSteps = nextSteps;
    }

    public List<String> getRecommendedProjects() {
        return recommendedProjects;
    }

    public void setRecommendedProjects(List<String> recommendedProjects) {
        this.recommendedProjects = recommendedProjects;
    }

    public List<String> getRecommendedCertifications() {
        return recommendedCertifications;
    }

    public void setRecommendedCertifications(List<String> recommendedCertifications) {
        this.recommendedCertifications = recommendedCertifications;
    }
}
