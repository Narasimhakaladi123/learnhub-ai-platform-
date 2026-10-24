package com.career.guidance.dto.response;

import com.career.guidance.entity.enums.QuestionType;
import java.util.List;
import java.util.UUID;

public class AssessmentQuestionDto {
    private UUID id;
    private String questionText;
    private QuestionType questionType;
    private List<String> options;
    private int points;

    public AssessmentQuestionDto() {
    }

    public AssessmentQuestionDto(UUID id, String questionText, QuestionType questionType, List<String> options, int points) {
        this.id = id;
        this.questionText = questionText;
        this.questionType = questionType;
        this.options = options;
        this.points = points;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
