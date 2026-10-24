package com.career.guidance.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class RoadmapDto {
    private UUID id;
    private String careerName;
    private int totalPhases;
    private int completedPhases;
    private int progressPercentage;
    private List<RoadmapItemDto> items;
    private LocalDateTime updatedAt;

    public RoadmapDto() {
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCareerName() {
        return careerName;
    }

    public void setCareerName(String careerName) {
        this.careerName = careerName;
    }

    public int getTotalPhases() {
        return totalPhases;
    }

    public void setTotalPhases(int totalPhases) {
        this.totalPhases = totalPhases;
    }

    public int getCompletedPhases() {
        return completedPhases;
    }

    public void setCompletedPhases(int completedPhases) {
        this.completedPhases = completedPhases;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public List<RoadmapItemDto> getItems() {
        return items;
    }

    public void setItems(List<RoadmapItemDto> items) {
        this.items = items;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
