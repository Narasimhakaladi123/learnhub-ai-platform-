package com.career.guidance.dto.response;

import com.career.guidance.entity.enums.PriorityLevel;
import com.career.guidance.entity.enums.ProficiencyLevel;
import java.util.UUID;

public class SkillGapDto {
    private UUID skillId;
    private String skillName;
    private ProficiencyLevel currentProficiency;
    private ProficiencyLevel requiredProficiency;
    private PriorityLevel priorityLevel;
    private String priorityLabel;
    private int sequenceOrder;

    public SkillGapDto() {
    }

    public SkillGapDto(UUID skillId, String skillName, ProficiencyLevel currentProficiency, ProficiencyLevel requiredProficiency, PriorityLevel priorityLevel, int sequenceOrder) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.currentProficiency = currentProficiency;
        this.requiredProficiency = requiredProficiency;
        this.priorityLevel = priorityLevel;
        this.priorityLabel = priorityLevel != null ? priorityLevel.getLabel() : null;
        this.sequenceOrder = sequenceOrder;
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

    public ProficiencyLevel getCurrentProficiency() {
        return currentProficiency;
    }

    public void setCurrentProficiency(ProficiencyLevel currentProficiency) {
        this.currentProficiency = currentProficiency;
    }

    public ProficiencyLevel getRequiredProficiency() {
        return requiredProficiency;
    }

    public void setRequiredProficiency(ProficiencyLevel requiredProficiency) {
        this.requiredProficiency = requiredProficiency;
    }

    public PriorityLevel getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
        if (priorityLevel != null) {
            this.priorityLabel = priorityLevel.getLabel();
        }
    }

    public String getPriorityLabel() {
        return priorityLabel;
    }

    public void setPriorityLabel(String priorityLabel) {
        this.priorityLabel = priorityLabel;
    }

    public int getSequenceOrder() {
        return sequenceOrder;
    }

    public void setSequenceOrder(int sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }
}
