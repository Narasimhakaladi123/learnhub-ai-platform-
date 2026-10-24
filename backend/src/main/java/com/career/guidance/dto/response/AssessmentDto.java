package com.career.guidance.dto.response;

import com.career.guidance.entity.enums.DifficultyLevel;
import java.util.List;
import java.util.UUID;

public class AssessmentDto {
    private UUID id;
    private String title;
    private String slug;
    private String category;
    private DifficultyLevel difficulty;
    private Integer timeLimitMinutes;
    private Integer passingScore;
    private String description;
    private int totalQuestions;
    private List<AssessmentQuestionDto> questions;

    public AssessmentDto() {
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getTimeLimitMinutes() {
        return timeLimitMinutes;
    }

    public void setTimeLimitMinutes(Integer timeLimitMinutes) {
        this.timeLimitMinutes = timeLimitMinutes;
    }

    public Integer getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(Integer passingScore) {
        this.passingScore = passingScore;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public List<AssessmentQuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<AssessmentQuestionDto> questions) {
        this.questions = questions;
    }
}
