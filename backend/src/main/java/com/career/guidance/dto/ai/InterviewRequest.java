package com.career.guidance.dto.ai;

import java.util.List;

public class InterviewRequest {
    private String role;
    private String difficulty;
    private List<String> topics;
    private int questionCount = 5;

    public InterviewRequest() {
    }

    public InterviewRequest(String role, String difficulty, List<String> topics, int questionCount) {
        this.role = role;
        this.difficulty = difficulty;
        this.topics = topics;
        this.questionCount = questionCount;
    }

    // Getters and Setters
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }
}
