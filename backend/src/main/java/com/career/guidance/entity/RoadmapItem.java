package com.career.guidance.entity;

import com.career.guidance.entity.enums.DifficultyLevel;
import com.career.guidance.entity.enums.RoadmapStatus;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "roadmap_items")
public class RoadmapItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id", nullable = false)
    private LearningRoadmap roadmap;

    @Column(name = "phase_number", nullable = false)
    private int phaseNumber;

    @Column(name = "phase_title", nullable = false, length = 150)
    private String phaseTitle;

    @Column(nullable = false, length = 150)
    private String topic;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DifficultyLevel difficulty = DifficultyLevel.INTERMEDIATE;

    @Column(name = "estimated_duration", length = 50)
    private String estimatedDuration;

    @Column(columnDefinition = "TEXT")
    private String prerequisites;

    @Column(columnDefinition = "TEXT")
    private String resources;

    @Column(name = "practice_tasks", columnDefinition = "TEXT")
    private String practiceTasks;

    @Column(name = "project_recommendation", columnDefinition = "TEXT")
    private String projectRecommendation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RoadmapStatus status = RoadmapStatus.NOT_STARTED;

    @Column(name = "sequence_order", nullable = false)
    private int sequenceOrder = 1;

    public RoadmapItem() {
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LearningRoadmap getRoadmap() {
        return roadmap;
    }

    public void setRoadmap(LearningRoadmap roadmap) {
        this.roadmap = roadmap;
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

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public String getPracticeTasks() {
        return practiceTasks;
    }

    public void setPracticeTasks(String practiceTasks) {
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
