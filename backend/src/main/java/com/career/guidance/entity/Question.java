package com.career.guidance.entity;

import com.career.guidance.entity.enums.QuestionType;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false, length = 30)
    private QuestionType questionType = QuestionType.MCQ;

    // JSON array of options e.g. ["A", "B", "C", "D"]
    @Column(name = "options_json", columnDefinition = "TEXT")
    private String optionsJson;

    // JSON array of correct indices or answers e.g. [0] or ["0"]
    @Column(name = "correct_answers_json", nullable = false, columnDefinition = "TEXT")
    private String correctAnswersJson;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @Column(nullable = false)
    private int points = 10;

    public Question() {
    }

    public Question(Assessment assessment, String questionText, QuestionType questionType, String optionsJson, String correctAnswersJson, String explanation, int points) {
        this.assessment = assessment;
        this.questionText = questionText;
        this.questionType = questionType;
        this.optionsJson = optionsJson;
        this.correctAnswersJson = correctAnswersJson;
        this.explanation = explanation;
        this.points = points;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
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

    public String getOptionsJson() {
        return optionsJson;
    }

    public void setOptionsJson(String optionsJson) {
        this.optionsJson = optionsJson;
    }

    public String getCorrectAnswersJson() {
        return correctAnswersJson;
    }

    public void setCorrectAnswersJson(String correctAnswersJson) {
        this.correctAnswersJson = correctAnswersJson;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
