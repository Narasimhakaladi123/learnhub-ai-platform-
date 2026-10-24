package com.career.guidance.dto.response;

import java.util.List;
import java.util.UUID;

public class CareerPathDto {
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private String category;
    private String averageSalaryRange;
    private String jobOutlook;
    private List<String> responsibilities;
    private List<String> typicalRoles;
    private List<CareerSkillDto> skills;
    private List<String> entrySkills;
    private List<String> intermediateSkills;
    private List<String> advancedSkills;
    private List<String> requiredSkills;
    private List<String> preferredSkills;

    public CareerPathDto() {
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

    public List<String> getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(List<String> responsibilities) {
        this.responsibilities = responsibilities;
    }

    public List<String> getTypicalRoles() {
        return typicalRoles;
    }

    public void setTypicalRoles(List<String> typicalRoles) {
        this.typicalRoles = typicalRoles;
    }

    public List<CareerSkillDto> getSkills() {
        return skills;
    }

    public void setSkills(List<CareerSkillDto> skills) {
        this.skills = skills;
    }

    public List<String> getEntrySkills() {
        return entrySkills;
    }

    public void setEntrySkills(List<String> entrySkills) {
        this.entrySkills = entrySkills;
    }

    public List<String> getIntermediateSkills() {
        return intermediateSkills;
    }

    public void setIntermediateSkills(List<String> intermediateSkills) {
        this.intermediateSkills = intermediateSkills;
    }

    public List<String> getAdvancedSkills() {
        return advancedSkills;
    }

    public void setAdvancedSkills(List<String> advancedSkills) {
        this.advancedSkills = advancedSkills;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public List<String> getPreferredSkills() {
        return preferredSkills;
    }

    public void setPreferredSkills(List<String> preferredSkills) {
        this.preferredSkills = preferredSkills;
    }
}
