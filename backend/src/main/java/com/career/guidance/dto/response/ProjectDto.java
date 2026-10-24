package com.career.guidance.dto.response;

import com.career.guidance.entity.enums.DifficultyLevel;
import java.util.List;
import java.util.UUID;

public class ProjectDto {
    private UUID id;
    private String title;
    private String description;
    private DifficultyLevel difficulty;
    private List<String> technologies;
    private List<String> skillsDeveloped;
    private List<String> features;
    private Integer estimatedDurationWeeks;
    private String targetCareerSlug;

    public ProjectDto() {
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
    }

    public List<String> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<String> technologies) {
        this.technologies = technologies;
    }

    public List<String> getSkillsDeveloped() {
        return skillsDeveloped;
    }

    public void setSkillsDeveloped(List<String> skillsDeveloped) {
        this.skillsDeveloped = skillsDeveloped;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public Integer getEstimatedDurationWeeks() {
        return estimatedDurationWeeks;
    }

    public void setEstimatedDurationWeeks(Integer estimatedDurationWeeks) {
        this.estimatedDurationWeeks = estimatedDurationWeeks;
    }

    public String getTargetCareerSlug() {
        return targetCareerSlug;
    }

    public void setTargetCareerSlug(String targetCareerSlug) {
        this.targetCareerSlug = targetCareerSlug;
    }
}
