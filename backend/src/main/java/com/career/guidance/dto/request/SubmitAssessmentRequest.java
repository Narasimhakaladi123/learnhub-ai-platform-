package com.career.guidance.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

public class SubmitAssessmentRequest {

    @NotNull(message = "Assessment ID is required")
    private UUID assessmentId;

    // Map of questionId -> selected answer (e.g. "0" or "A, B" or code string)
    private Map<UUID, String> answers;

    private Integer timeTakenSeconds;

    public SubmitAssessmentRequest() {
    }

    public SubmitAssessmentRequest(UUID assessmentId, Map<UUID, String> answers, Integer timeTakenSeconds) {
        this.assessmentId = assessmentId;
        this.answers = answers;
        this.timeTakenSeconds = timeTakenSeconds;
    }

    // Getters and Setters
    public UUID getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(UUID assessmentId) {
        this.assessmentId = assessmentId;
    }

    public Map<UUID, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<UUID, String> answers) {
        this.answers = answers;
    }

    public Integer getTimeTakenSeconds() {
        return timeTakenSeconds;
    }

    public void setTimeTakenSeconds(Integer timeTakenSeconds) {
        this.timeTakenSeconds = timeTakenSeconds;
    }
}
