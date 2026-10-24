package com.career.guidance.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "career_recommendations")
public class CareerRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private StudentProfile profile;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "career_id", nullable = false)
    private CareerPath careerPath;

    @Column(name = "career_name", nullable = false, length = 100)
    private String careerName;

    @Column(name = "compatibility_score", nullable = false)
    private int compatibilityScore;

    @Column(name = "matching_skills", columnDefinition = "TEXT")
    private String matchingSkillsJson;

    @Column(name = "missing_skills", columnDefinition = "TEXT")
    private String missingSkillsJson;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "next_steps", columnDefinition = "TEXT")
    private String nextStepsJson;

    @Column(name = "is_target_career", nullable = false)
    private boolean targetCareer = false;

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt = LocalDateTime.now();

    public CareerRecommendation() {
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public StudentProfile getProfile() {
        return profile;
    }

    public void setProfile(StudentProfile profile) {
        this.profile = profile;
    }

    public CareerPath getCareerPath() {
        return careerPath;
    }

    public void setCareerPath(CareerPath careerPath) {
        this.careerPath = careerPath;
    }

    public String getCareerName() {
        return careerName;
    }

    public void setCareerName(String careerName) {
        this.careerName = careerName;
    }

    public int getCompatibilityScore() {
        return compatibilityScore;
    }

    public void setCompatibilityScore(int compatibilityScore) {
        this.compatibilityScore = compatibilityScore;
    }

    public String getMatchingSkillsJson() {
        return matchingSkillsJson;
    }

    public void setMatchingSkillsJson(String matchingSkillsJson) {
        this.matchingSkillsJson = matchingSkillsJson;
    }

    public String getMissingSkillsJson() {
        return missingSkillsJson;
    }

    public void setMissingSkillsJson(String missingSkillsJson) {
        this.missingSkillsJson = missingSkillsJson;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getNextStepsJson() {
        return nextStepsJson;
    }

    public void setNextStepsJson(String nextStepsJson) {
        this.nextStepsJson = nextStepsJson;
    }

    public boolean isTargetCareer() {
        return targetCareer;
    }

    public void setTargetCareer(boolean targetCareer) {
        this.targetCareer = targetCareer;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
}
