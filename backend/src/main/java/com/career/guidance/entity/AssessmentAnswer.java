package com.career.guidance.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "assessment_answers")
public class AssessmentAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false)
    private AssessmentAttempt attempt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "selected_answers", columnDefinition = "TEXT")
    private String selectedAnswers;

    @Column(name = "is_correct", nullable = false)
    private boolean correct;

    @Column(name = "points_awarded", nullable = false)
    private int pointsAwarded;

    public AssessmentAnswer() {
    }

    public AssessmentAnswer(AssessmentAttempt attempt, Question question, String selectedAnswers, boolean correct, int pointsAwarded) {
        this.attempt = attempt;
        this.question = question;
        this.selectedAnswers = selectedAnswers;
        this.correct = correct;
        this.pointsAwarded = pointsAwarded;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public AssessmentAttempt getAttempt() {
        return attempt;
    }

    public void setAttempt(AssessmentAttempt attempt) {
        this.attempt = attempt;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getSelectedAnswers() {
        return selectedAnswers;
    }

    public void setSelectedAnswers(String selectedAnswers) {
        this.selectedAnswers = selectedAnswers;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public int getPointsAwarded() {
        return pointsAwarded;
    }

    public void setPointsAwarded(int pointsAwarded) {
        this.pointsAwarded = pointsAwarded;
    }
}
