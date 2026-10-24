package com.career.guidance.entity;

import com.career.guidance.entity.enums.ProficiencyLevel;
import com.career.guidance.entity.enums.RequirementLevel;
import com.career.guidance.entity.enums.SkillStage;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "career_skills", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"career_id", "skill_id"})
})
public class CareerSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "career_id", nullable = false)
    private CareerPath careerPath;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Enumerated(EnumType.STRING)
    @Column(name = "requirement_level", nullable = false, length = 30)
    private RequirementLevel requirementLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "skill_stage", nullable = false, length = 30)
    private SkillStage skillStage;

    @Enumerated(EnumType.STRING)
    @Column(name = "minimum_proficiency", nullable = false, length = 30)
    private ProficiencyLevel minimumProficiency = ProficiencyLevel.INTERMEDIATE;

    @Column(nullable = false)
    private int weight = 5;

    public CareerSkill() {
    }

    public CareerSkill(CareerPath careerPath, Skill skill, RequirementLevel requirementLevel, SkillStage skillStage, ProficiencyLevel minimumProficiency, int weight) {
        this.careerPath = careerPath;
        this.skill = skill;
        this.requirementLevel = requirementLevel;
        this.skillStage = skillStage;
        this.minimumProficiency = minimumProficiency;
        this.weight = weight;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public CareerPath getCareerPath() {
        return careerPath;
    }

    public void setCareerPath(CareerPath careerPath) {
        this.careerPath = careerPath;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
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
