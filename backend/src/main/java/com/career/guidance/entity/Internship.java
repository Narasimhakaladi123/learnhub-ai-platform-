package com.career.guidance.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "internships")
public class Internship {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private StudentProfile profile;

    @Column(nullable = false, length = 150)
    private String company;

    @Column(nullable = false, length = 100)
    private String role;

    @Column(length = 50)
    private String duration;

    @Column(length = 255)
    private String technologies;

    @Column(columnDefinition = "TEXT")
    private String description;

    public Internship() {
    }

    public Internship(StudentProfile profile, String company, String role, String duration, String technologies, String description) {
        this.profile = profile;
        this.company = company;
        this.role = role;
        this.duration = duration;
        this.technologies = technologies;
        this.description = description;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTechnologies() {
        return technologies;
    }

    public void setTechnologies(String technologies) {
        this.technologies = technologies;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
