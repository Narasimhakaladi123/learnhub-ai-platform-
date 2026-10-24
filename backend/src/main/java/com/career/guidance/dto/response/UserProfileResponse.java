package com.career.guidance.dto.response;

import com.career.guidance.entity.enums.InterestArea;
import com.career.guidance.entity.enums.RoleType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class UserProfileResponse {
    private UUID userId;
    private String email;
    private String fullName;
    private String phone;
    private RoleType role;
    private UUID profileId;
    private String degree;
    private String branch;
    private String college;
    private Integer graduationYear;
    private BigDecimal cgpa;
    private String targetCareer;
    private Integer readinessPercentage;
    private String bio;
    private String location;
    private List<StudentSkillDto> skills;
    private List<InterestArea> interests;
    private List<Object> educations;
    private List<Object> experiences;
    private List<Object> internships;
    private List<Object> projects;
    private List<Object> certifications;
    private LocalDateTime createdAt;

    public UserProfileResponse() {
    }

    // Getters and Setters
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public UUID getProfileId() {
        return profileId;
    }

    public void setProfileId(UUID profileId) {
        this.profileId = profileId;
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

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    public BigDecimal getCgpa() {
        return cgpa;
    }

    public void setCgpa(BigDecimal cgpa) {
        this.cgpa = cgpa;
    }

    public String getTargetCareer() {
        return targetCareer;
    }

    public void setTargetCareer(String targetCareer) {
        this.targetCareer = targetCareer;
    }

    public Integer getReadinessPercentage() {
        return readinessPercentage;
    }

    public void setReadinessPercentage(Integer readinessPercentage) {
        this.readinessPercentage = readinessPercentage;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<StudentSkillDto> getSkills() {
        return skills;
    }

    public void setSkills(List<StudentSkillDto> skills) {
        this.skills = skills;
    }

    public List<InterestArea> getInterests() {
        return interests;
    }

    public void setInterests(List<InterestArea> interests) {
        this.interests = interests;
    }

    public List<Object> getEducations() {
        return educations;
    }

    public void setEducations(List<Object> educations) {
        this.educations = educations;
    }

    public List<Object> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Object> experiences) {
        this.experiences = experiences;
    }

    public List<Object> getInternships() {
        return internships;
    }

    public void setInternships(List<Object> internships) {
        this.internships = internships;
    }

    public List<Object> getProjects() {
        return projects;
    }

    public void setProjects(List<Object> projects) {
        this.projects = projects;
    }

    public List<Object> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<Object> certifications) {
        this.certifications = certifications;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
