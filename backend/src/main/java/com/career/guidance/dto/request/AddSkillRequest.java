package com.career.guidance.dto.request;

import com.career.guidance.entity.enums.ProficiencyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class AddSkillRequest {
    private UUID skillId;

    @NotBlank(message = "Skill name is required")
    private String skillName;

    private String category;

    @NotNull(message = "Proficiency level is required")
    private ProficiencyLevel proficiency;

    private Integer experienceMonths = 0;

    public AddSkillRequest() {
    }

    public AddSkillRequest(UUID skillId, String skillName, String category, ProficiencyLevel proficiency, Integer experienceMonths) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.category = category;
        this.proficiency = proficiency;
        this.experienceMonths = experienceMonths;
    }

    // Getters and Setters
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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
