package com.career.guidance.dto.ai;

import java.util.List;
import java.util.Map;

public class SkillGapRequest {
    private String careerName;
    private Map<String, String> studentSkills; // skill -> proficiency
    private List<String> requiredSkills;
    private List<String> preferredSkills;

    public SkillGapRequest() {
    }

    public SkillGapRequest(String careerName, Map<String, String> studentSkills, List<String> requiredSkills, List<String> preferredSkills) {
        this.careerName = careerName;
        this.studentSkills = studentSkills;
        this.requiredSkills = requiredSkills;
        this.preferredSkills = preferredSkills;
    }

    // Getters and Setters
    public String getCareerName() {
        return careerName;
    }

    public void setCareerName(String careerName) {
        this.careerName = careerName;
    }

    public Map<String, String> getStudentSkills() {
        return studentSkills;
    }

    public void setStudentSkills(Map<String, String> studentSkills) {
        this.studentSkills = studentSkills;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public List<String> getPreferredSkills() {
        return preferredSkills;
    }

    public void setPreferredSkills(List<String> preferredSkills) {
        this.preferredSkills = preferredSkills;
    }
}
