package com.career.guidance.entity;

import com.career.guidance.entity.enums.SkillCategory;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "skills", indexes = {
    @Index(name = "idx_skill_name", columnList = "name")
})
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SkillCategory category;

    @Column(length = 255)
    private String description;

    public Skill() {
    }

    public Skill(String name, SkillCategory category, String description) {
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
