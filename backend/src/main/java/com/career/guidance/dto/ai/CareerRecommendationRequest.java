package com.career.guidance.dto.ai;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class CareerRecommendationRequest {
    private String studentName;
    private String degree;
    private String branch;
    private BigDecimal cgpa;
    private List<String> interests;
    private Map<String, String> skillsWithProficiency; // skillName -> proficiency
    private List<String> projects;
    private List<String> experiences;
    private List<String> certifications;
    private Map<String, Integer> assessmentScores; // category -> score
    private String targetCareerPreference;
    private List<String> availableCareerOptions;

    public CareerRecommendationRequest() {
    }

    // Getters and Setters
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public BigDecimal getCgpa() {
        return cgpa;
    }

    public void setCgpa(BigDecimal cgpa) {
        this.cgpa = cgpa;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public Map<String, String> getSkillsWithProficiency() {
        return skillsWithProficiency;
    }

    public void setSkillsWithProficiency(Map<String, String> skillsWithProficiency) {
        this.skillsWithProficiency = skillsWithProficiency;
    }

    public List<String> getProjects() {
        return projects;
    }

    public void setProjects(List<String> projects) {
        this.projects = projects;
    }

    public List<String> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<String> experiences) {
        this.experiences = experiences;
    }

    public List<String> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<String> certifications) {
        this.certifications = certifications;
    }

    public Map<String, Integer> getAssessmentScores() {
        return assessmentScores;
    }

    public void setAssessmentScores(Map<String, Integer> assessmentScores) {
        this.assessmentScores = assessmentScores;
    }

    public String getTargetCareerPreference() {
        return targetCareerPreference;
    }

    public void setTargetCareerPreference(String targetCareerPreference) {
        this.targetCareerPreference = targetCareerPreference;
    }

    public List<String> getAvailableCareerOptions() {
        return availableCareerOptions;
    }

    public void setAvailableCareerOptions(List<String> availableCareerOptions) {
        this.availableCareerOptions = availableCareerOptions;
    }
}
