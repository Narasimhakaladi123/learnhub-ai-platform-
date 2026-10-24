package com.career.guidance.dto.request;

import jakarta.validation.constraints.NotBlank;

public class AddProjectRequest {

    @NotBlank(message = "Project name is required")
    private String projectName;

    private String description;

    @NotBlank(message = "Technologies are required")
    private String technologies;

    private String githubUrl;
    private String liveUrl;
    private String role;

    public AddProjectRequest() {
    }

    // Getters and Setters
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechnologies() {
        return technologies;
    }

    public void setTechnologies(String technologies) {
        this.technologies = technologies;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
