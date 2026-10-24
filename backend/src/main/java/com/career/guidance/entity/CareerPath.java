package com.career.guidance.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "career_paths", indexes = {
    @Index(name = "idx_career_slug", columnList = "slug")
})
public class CareerPath {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(length = 50)
    private String category;

    @Column(name = "average_salary_range", length = 100)
    private String averageSalaryRange;

    @Column(name = "job_outlook", length = 100)
    private String jobOutlook;

    @Column(columnDefinition = "TEXT")
    private String responsibilities;

    @Column(name = "typical_roles", columnDefinition = "TEXT")
    private String typicalRoles;

    @OneToMany(mappedBy = "careerPath", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CareerSkill> careerSkills = new ArrayList<>();

    public CareerPath() {
    }

    public CareerPath(String name, String slug, String description, String category, String averageSalaryRange, String jobOutlook) {
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.category = category;
        this.averageSalaryRange = averageSalaryRange;
        this.jobOutlook = jobOutlook;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAverageSalaryRange() {
        return averageSalaryRange;
    }

    public void setAverageSalaryRange(String averageSalaryRange) {
        this.averageSalaryRange = averageSalaryRange;
    }

    public String getJobOutlook() {
        return jobOutlook;
    }

    public void setJobOutlook(String jobOutlook) {
        this.jobOutlook = jobOutlook;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }

    public String getTypicalRoles() {
        return typicalRoles;
    }

    public void setTypicalRoles(String typicalRoles) {
        this.typicalRoles = typicalRoles;
    }

    public List<CareerSkill> getCareerSkills() {
        return careerSkills;
    }

    public void setCareerSkills(List<CareerSkill> careerSkills) {
        this.careerSkills = careerSkills;
    }
}
