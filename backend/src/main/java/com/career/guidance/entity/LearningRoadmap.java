package com.career.guidance.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "learning_roadmaps")
public class LearningRoadmap {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private StudentProfile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "career_id", nullable = false)
    private CareerPath careerPath;

    @Column(name = "career_name", nullable = false, length = 100)
    private String careerName;

    @Column(name = "total_phases", nullable = false)
    private int totalPhases = 0;

    @Column(name = "completed_phases", nullable = false)
    private int completedPhases = 0;

    @Column(name = "progress_percentage", nullable = false)
    private int progressPercentage = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "roadmap", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("sequenceOrder ASC")
    private List<RoadmapItem> items = new ArrayList<>();

    public LearningRoadmap() {
    }

    public LearningRoadmap(StudentProfile profile, CareerPath careerPath, String careerName) {
        this.profile = profile;
        this.careerPath = careerPath;
        this.careerName = careerName;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void recalculateProgress() {
        if (items.isEmpty()) {
            this.progressPercentage = 0;
            return;
        }
        long completed = items.stream().filter(i -> i.getStatus() == com.career.guidance.entity.enums.RoadmapStatus.COMPLETED).count();
        this.completedPhases = (int) completed;
        this.totalPhases = items.size();
        this.progressPercentage = (int) Math.round(((double) completed / items.size()) * 100);
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<RoadmapItem> getItems() {
        return items;
    }

    public void setItems(List<RoadmapItem> items) {
        this.items = items;
    }
}
