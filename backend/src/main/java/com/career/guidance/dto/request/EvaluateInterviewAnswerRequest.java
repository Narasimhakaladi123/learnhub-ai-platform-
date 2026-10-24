package com.career.guidance.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class EvaluateInterviewAnswerRequest {

    @NotNull(message = "Question ID is required")
    private UUID questionId;

    @NotBlank(message = "User answer is required")
    private String answer;

    public EvaluateInterviewAnswerRequest() {
    }

    public EvaluateInterviewAnswerRequest(UUID questionId, String answer) {
        this.questionId = questionId;
        this.answer = answer;
    }

    // Getters and Setters
    public UUID getQuestionId() {
        return questionId;
    }

    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
