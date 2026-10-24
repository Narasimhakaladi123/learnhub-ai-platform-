package com.career.guidance.dto.response;

import com.career.guidance.entity.enums.ProficiencyLevel;
import com.career.guidance.entity.enums.RequirementLevel;
import com.career.guidance.entity.enums.SkillStage;
import java.util.UUID;

public class CareerSkillDto {
    private UUID skillId;
    private String skillName;
    private String category;
    private RequirementLevel requirementLevel;
    private SkillStage skillStage;
    private ProficiencyLevel minimumProficiency;
    private int weight;

    public CareerSkillDto() {
    }

    public CareerSkillDto(UUID skillId, String skillName, String category, RequirementLevel requirementLevel, SkillStage skillStage, ProficiencyLevel minimumProficiency, int weight) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.category = category;
        this.requirementLevel = requirementLevel;
        this.skillStage = skillStage;
        this.minimumProficiency = minimumProficiency;
        this.weight = weight;
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

    public RequirementLevel getRequirementLevel() {
        return requirementLevel;
    }

    public void setRequirementLevel(RequirementLevel requirementLevel) {
        this.requirementLevel = requirementLevel;
    }

    public SkillStage getSkillStage() {
        return skillStage;
    }

    public void setSkillStage(SkillStage skillStage) {
        this.skillStage = skillStage;
    }

    public ProficiencyLevel getMinimumProficiency() {
        return minimumProficiency;
    }

    public void setMinimumProficiency(ProficiencyLevel minimumProficiency) {
        this.minimumProficiency = minimumProficiency;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
