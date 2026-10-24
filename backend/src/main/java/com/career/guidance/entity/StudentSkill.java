package com.career.guidance.entity;

import com.career.guidance.entity.enums.ProficiencyLevel;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "student_skills", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"profile_id", "skill_id"})
})
public class StudentSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private StudentProfile profile;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProficiencyLevel proficiency;

    @Column(name = "experience_months")
    private Integer experienceMonths = 0;

    public StudentSkill() {
    }

    public StudentSkill(StudentProfile profile, Skill skill, ProficiencyLevel proficiency, Integer experienceMonths) {
        this.profile = profile;
        this.skill = skill;
        this.proficiency = proficiency;
        this.experienceMonths = experienceMonths != null ? experienceMonths : 0;
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

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
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
