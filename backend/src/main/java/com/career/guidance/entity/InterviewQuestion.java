package com.career.guidance.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "interview_questions")
public class InterviewQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private InterviewSession session;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "expected_concepts", columnDefinition = "TEXT")
    private String expectedConcepts;

    @Column(length = 30)
    private String difficulty;

    @Column(name = "follow_up_questions", columnDefinition = "TEXT")
    private String followUpQuestions;

    @Column(name = "user_answer", columnDefinition = "TEXT")
    private String userAnswer;

    @Column(columnDefinition = "TEXT")
    private String strengths;

    @Column(name = "missing_concepts", columnDefinition = "TEXT")
    private String missingConcepts;

    @Column(name = "suggested_structure", columnDefinition = "TEXT")
    private String suggestedStructure;

    @Column(name = "areas_to_improve", columnDefinition = "TEXT")
    private String areasToImprove;

    @Column(name = "evaluation_score")
    private Integer evaluationScore;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    public InterviewQuestion() {
    }

    public InterviewQuestion(InterviewSession session, String questionText, String expectedConcepts, String difficulty, String followUpQuestions) {
        this.session = session;
        this.questionText = questionText;
        this.expectedConcepts = expectedConcepts;
        this.difficulty = difficulty;
        this.followUpQuestions = followUpQuestions;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public InterviewSession getSession() {
        return session;
    }

    public void setSession(InterviewSession session) {
        this.session = session;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getExpectedConcepts() {
        return expectedConcepts;
    }

    public void setExpectedConcepts(String expectedConcepts) {
        this.expectedConcepts = expectedConcepts;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getFollowUpQuestions() {
        return followUpQuestions;
    }

    public void setFollowUpQuestions(String followUpQuestions) {
        this.followUpQuestions = followUpQuestions;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getStrengths() {
        return strengths;
    }

    public void setStrengths(String strengths) {
        this.strengths = strengths;
    }

    public String getMissingConcepts() {
        return missingConcepts;
    }

    public void setMissingConcepts(String missingConcepts) {
        this.missingConcepts = missingConcepts;
    }

    public String getSuggestedStructure() {
        return suggestedStructure;
    }

    public void setSuggestedStructure(String suggestedStructure) {
        this.suggestedStructure = suggestedStructure;
    }

    public String getAreasToImprove() {
        return areasToImprove;
    }

    public void setAreasToImprove(String areasToImprove) {
        this.areasToImprove = areasToImprove;
    }

    public Integer getEvaluationScore() {
        return evaluationScore;
    }

    public void setEvaluationScore(Integer evaluationScore) {
        this.evaluationScore = evaluationScore;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
