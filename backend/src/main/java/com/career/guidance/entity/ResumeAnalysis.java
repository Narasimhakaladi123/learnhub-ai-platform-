package com.career.guidance.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "resume_analyses")
public class ResumeAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false, unique = true)
    private Resume resume;

    @Column(name = "target_role", length = 100)
    private String targetRole;

    @Column(name = "detected_skills", columnDefinition = "TEXT")
    private String detectedSkills;

    @Column(name = "missing_information", columnDefinition = "TEXT")
    private String missingInformation;

    @Column(name = "skill_gaps", columnDefinition = "TEXT")
    private String skillGaps;

    @Column(name = "suggested_improvements", columnDefinition = "TEXT")
    private String suggestedImprovements;

    @Column(name = "job_role_compatibility", columnDefinition = "TEXT")
    private String jobRoleCompatibility;

    @Column(name = "ats_keyword_analysis", columnDefinition = "TEXT")
    private String atsKeywordAnalysis;

    @Column(name = "match_score")
    private Integer matchScore;

    @Column(name = "analyzed_at", nullable = false)
    private LocalDateTime analyzedAt = LocalDateTime.now();

    public ResumeAnalysis() {
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public String getTargetRole() {
        return targetRole;
    }

    public void setTargetRole(String targetRole) {
        this.targetRole = targetRole;
    }

    public String getDetectedSkills() {
        return detectedSkills;
    }

    public void setDetectedSkills(String detectedSkills) {
        this.detectedSkills = detectedSkills;
    }

    public String getMissingInformation() {
        return missingInformation;
    }

    public void setMissingInformation(String missingInformation) {
        this.missingInformation = missingInformation;
    }

    public String getSkillGaps() {
        return skillGaps;
    }

    public void setSkillGaps(String skillGaps) {
        this.skillGaps = skillGaps;
    }

    public String getSuggestedImprovements() {
        return suggestedImprovements;
    }

    public void setSuggestedImprovements(String suggestedImprovements) {
        this.suggestedImprovements = suggestedImprovements;
    }

    public String getJobRoleCompatibility() {
        return jobRoleCompatibility;
    }

    public void setJobRoleCompatibility(String jobRoleCompatibility) {
        this.jobRoleCompatibility = jobRoleCompatibility;
    }

    public String getAtsKeywordAnalysis() {
        return atsKeywordAnalysis;
    }

    public void setAtsKeywordAnalysis(String atsKeywordAnalysis) {
        this.atsKeywordAnalysis = atsKeywordAnalysis;
    }

    public Integer getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(Integer matchScore) {
        this.matchScore = matchScore;
    }

    public LocalDateTime getAnalyzedAt() {
        return analyzedAt;
    }

    public void setAnalyzedAt(LocalDateTime analyzedAt) {
        this.analyzedAt = analyzedAt;
    }
}
