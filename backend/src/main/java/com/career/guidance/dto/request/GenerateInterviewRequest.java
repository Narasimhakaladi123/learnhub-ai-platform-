package com.career.guidance.dto.request;

import com.career.guidance.entity.enums.DifficultyLevel;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class GenerateInterviewRequest {

    @NotBlank(message = "Role is required")
    private String role; // e.g. "Java Backend Developer"

    private DifficultyLevel difficulty = DifficultyLevel.INTERMEDIATE;

    private List<String> topics; // e.g. ["Java", "Spring Boot", "SQL"]

    public GenerateInterviewRequest() {
    }

    public GenerateInterviewRequest(String role, DifficultyLevel difficulty, List<String> topics) {
        this.role = role;
        this.difficulty = difficulty;
        this.topics = topics;
    }

    // Getters and Setters
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
}
