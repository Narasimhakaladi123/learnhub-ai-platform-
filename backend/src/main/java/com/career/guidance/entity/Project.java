package com.career.guidance.entity;

import com.career.guidance.entity.enums.DifficultyLevel;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DifficultyLevel difficulty;

    @Column(nullable = false, length = 255)
    private String technologies;

    @Column(name = "skills_developed", columnDefinition = "TEXT")
    private String skillsDeveloped;

    @Column(columnDefinition = "TEXT")
    private String features;

    @Column(name = "estimated_duration_weeks")
    private Integer estimatedDurationWeeks;

    @Column(name = "target_career_slug", length = 100)
    private String targetCareerSlug;

    public Project() {
    }

    public Project(String title, String description, DifficultyLevel difficulty, String technologies, String skillsDeveloped, String features, Integer estimatedDurationWeeks, String targetCareerSlug) {
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.technologies = technologies;
        this.skillsDeveloped = skillsDeveloped;
        this.features = features;
        this.estimatedDurationWeeks = estimatedDurationWeeks;
        this.targetCareerSlug = targetCareerSlug;
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

    public String getTechnologies() {
        return technologies;
    }

    public void setTechnologies(String technologies) {
        this.technologies = technologies;
    }

    public String getSkillsDeveloped() {
        return skillsDeveloped;
    }

    public void setSkillsDeveloped(String skillsDeveloped) {
        this.skillsDeveloped = skillsDeveloped;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
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
