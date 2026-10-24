package com.career.guidance.entity;

import com.career.guidance.entity.enums.PriorityLevel;
import com.career.guidance.entity.enums.ProficiencyLevel;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "skill_gaps")
public class SkillGap {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private StudentProfile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "career_id", nullable = false)
    private CareerPath careerPath;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(name = "skill_name", nullable = false, length = 100)
    private String skillName;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_proficiency", length = 30)
    private ProficiencyLevel currentProficiency;

    @Enumerated(EnumType.STRING)
    @Column(name = "required_proficiency", nullable = false, length = 30)
    private ProficiencyLevel requiredProficiency;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority_level", nullable = false, length = 30)
    private PriorityLevel priorityLevel;

    @Column(name = "sequence_order", nullable = false)
    private int sequenceOrder = 1;

    public SkillGap() {
    }

    public SkillGap(StudentProfile profile, CareerPath careerPath, Skill skill, String skillName, ProficiencyLevel currentProficiency, ProficiencyLevel requiredProficiency, PriorityLevel priorityLevel, int sequenceOrder) {
        this.profile = profile;
        this.careerPath = careerPath;
        this.skill = skill;
        this.skillName = skillName;
        this.currentProficiency = currentProficiency;
        this.requiredProficiency = requiredProficiency;
        this.priorityLevel = priorityLevel;
        this.sequenceOrder = sequenceOrder;
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
    }

    public int getSequenceOrder() {
        return sequenceOrder;
    }

    public void setSequenceOrder(int sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }
}
