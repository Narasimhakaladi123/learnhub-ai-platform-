package com.career.guidance.dto.response;

import com.career.guidance.entity.enums.ProficiencyLevel;
import com.career.guidance.entity.enums.SkillCategory;
import java.util.UUID;

public class StudentSkillDto {
    private UUID id;
    private UUID skillId;
    private String skillName;
    private SkillCategory category;
    private ProficiencyLevel proficiency;
    private Integer experienceMonths;

    public StudentSkillDto() {
    }

    public StudentSkillDto(UUID id, UUID skillId, String skillName, SkillCategory category, ProficiencyLevel proficiency, Integer experienceMonths) {
        this.id = id;
        this.skillId = skillId;
        this.skillName = skillName;
        this.category = category;
        this.proficiency = proficiency;
        this.experienceMonths = experienceMonths;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSkillId() {
        return skillId;
    }

    public void setSkillId(UUID skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public SkillCategory getCategory() {
        return category;
    }

    public void setCategory(SkillCategory category) {
        this.category = category;
    }

    public ProficiencyLevel getProficiency() {
        return proficiency;
    }

    public void setProficiency(ProficiencyLevel proficiency) {
        this.proficiency = proficiency;
    }

    public Integer getExperienceMonths() {
        return experienceMonths;
    }

    public void setExperienceMonths(Integer experienceMonths) {
        this.experienceMonths = experienceMonths;
    }
}
