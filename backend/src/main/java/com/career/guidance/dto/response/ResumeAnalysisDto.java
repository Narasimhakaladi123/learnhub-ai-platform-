package com.career.guidance.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ResumeAnalysisDto {
    private UUID id;
    private UUID resumeId;
    private String fileName;
    private String targetRole;
    private Integer matchScore;
    private List<String> detectedSkills;
    private List<String> missingInformation;
    private List<String> skillGaps;
    private List<String> suggestedImprovements;
    private String jobRoleCompatibility;
    private Map<String, Object> atsKeywordAnalysis;
    private LocalDateTime analyzedAt;
    private LocalDateTime uploadedAt;

    public ResumeAnalysisDto() {
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getResumeId() {
        return resumeId;
    }

    public void setResumeId(UUID resumeId) {
        this.resumeId = resumeId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTargetRole() {
        return targetRole;
    }

    public void setTargetRole(String targetRole) {
        this.targetRole = targetRole;
    }

    public Integer getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(Integer matchScore) {
        this.matchScore = matchScore;
    }

    public List<String> getDetectedSkills() {
        return detectedSkills;
    }

    public void setDetectedSkills(List<String> detectedSkills) {
        this.detectedSkills = detectedSkills;
    }

    public List<String> getMissingInformation() {
        return missingInformation;
    }

    public void setMissingInformation(List<String> missingInformation) {
        this.missingInformation = missingInformation;
    }

    public List<String> getSkillGaps() {
        return skillGaps;
    }

    public void setSkillGaps(List<String> skillGaps) {
        this.skillGaps = skillGaps;
    }

    public List<String> getSuggestedImprovements() {
        return suggestedImprovements;
    }

    public void setSuggestedImprovements(List<String> suggestedImprovements) {
        this.suggestedImprovements = suggestedImprovements;
    }

    public String getJobRoleCompatibility() {
        return jobRoleCompatibility;
    }

    public void setJobRoleCompatibility(String jobRoleCompatibility) {
        this.jobRoleCompatibility = jobRoleCompatibility;
    }

    public Map<String, Object> getAtsKeywordAnalysis() {
        return atsKeywordAnalysis;
    }

    public void setAtsKeywordAnalysis(Map<String, Object> atsKeywordAnalysis) {
        this.atsKeywordAnalysis = atsKeywordAnalysis;
    }

    public LocalDateTime getAnalyzedAt() {
        return analyzedAt;
    }

    public void setAnalyzedAt(LocalDateTime analyzedAt) {
        this.analyzedAt = analyzedAt;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
