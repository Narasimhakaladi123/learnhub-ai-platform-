package com.career.guidance.dto.response;

import java.util.List;
import java.util.UUID;

public class InterviewEvaluationDto {
    private UUID questionId;
    private String strengths;
    private List<String> missingConcepts;
    private String suggestedStructure;
    private List<String> areasToImprove;
    private int score;
    private String feedback;
    private String disclaimer = "This evaluation is an AI-generated assessment for practice and learning purposes only. It is not an official hiring or employment decision.";

    public InterviewEvaluationDto() {
    }

    // Getters and Setters
    public UUID getQuestionId() {
        return questionId;
    }

    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
    }

    public String getStrengths() {
        return strengths;
    }

    public void setStrengths(String strengths) {
        this.strengths = strengths;
    }

    public List<String> getMissingConcepts() {
        return missingConcepts;
    }

    public void setMissingConcepts(List<String> missingConcepts) {
        this.missingConcepts = missingConcepts;
    }

    public String getSuggestedStructure() {
        return suggestedStructure;
    }

    public void setSuggestedStructure(String suggestedStructure) {
        this.suggestedStructure = suggestedStructure;
    }

    public List<String> getAreasToImprove() {
        return areasToImprove;
    }

    public void setAreasToImprove(List<String> areasToImprove) {
        this.areasToImprove = areasToImprove;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }
}
