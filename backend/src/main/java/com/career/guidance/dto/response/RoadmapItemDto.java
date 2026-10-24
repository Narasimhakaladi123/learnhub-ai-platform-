package com.career.guidance.dto.response;

import com.career.guidance.entity.enums.DifficultyLevel;
import com.career.guidance.entity.enums.RoadmapStatus;
import java.util.List;
import java.util.UUID;

public class RoadmapItemDto {
    private UUID id;
    private int phaseNumber;
    private String phaseTitle;
    private String topic;
    private String description;
    private DifficultyLevel difficulty;
    private String estimatedDuration;
    private List<String> prerequisites;
    private List<String> resources;
    private List<String> practiceTasks;
    private String projectRecommendation;
    private RoadmapStatus status;
    private int sequenceOrder;

    public RoadmapItemDto() {
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getPhaseNumber() {
        return phaseNumber;
    }

    public void setPhaseNumber(int phaseNumber) {
        this.phaseNumber = phaseNumber;
    }

    public String getPhaseTitle() {
        return phaseTitle;
    }

    public void setPhaseTitle(String phaseTitle) {
        this.phaseTitle = phaseTitle;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
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

    public String getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(String estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public List<String> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<String> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public List<String> getResources() {
        return resources;
    }

    public void setResources(List<String> resources) {
        this.resources = resources;
    }

    public List<String> getPracticeTasks() {
        return practiceTasks;
    }

    public void setPracticeTasks(List<String> practiceTasks) {
        this.practiceTasks = practiceTasks;
    }

    public String getProjectRecommendation() {
        return projectRecommendation;
    }

    public void setProjectRecommendation(String projectRecommendation) {
        this.projectRecommendation = projectRecommendation;
    }

    public RoadmapStatus getStatus() {
        return status;
    }

    public void setStatus(RoadmapStatus status) {
        this.status = status;
    }

    public int getSequenceOrder() {
        return sequenceOrder;
    }

    public void setSequenceOrder(int sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }
}
