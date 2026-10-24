package com.career.guidance.dto.response;

import com.career.guidance.entity.enums.SkillCategory;
import java.util.UUID;

public class SkillDto {
    private UUID id;
    private String name;
    private SkillCategory category;
    private String description;

    public SkillDto() {
    }

    public SkillDto(UUID id, String name, SkillCategory category, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
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

    public SkillCategory getCategory() {
        return category;
    }

    public void setCategory(SkillCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
