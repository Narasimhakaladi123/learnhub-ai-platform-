package com.career.guidance.dto.ai;

import java.util.List;

public class InterviewResponse {
    private String role;
    private String difficulty;
    private List<AIQuestionItem> questions;

    public static class AIQuestionItem {
        private String question;
        private List<String> expectedConcepts;
        private String difficulty;
        private List<String> followUpQuestions;

        public AIQuestionItem() {
        }

        public AIQuestionItem(String question, List<String> expectedConcepts, String difficulty, List<String> followUpQuestions) {
            this.question = question;
            this.expectedConcepts = expectedConcepts;
            this.difficulty = difficulty;
            this.followUpQuestions = followUpQuestions;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public List<String> getExpectedConcepts() {
            return expectedConcepts;
        }

        public void setExpectedConcepts(List<String> expectedConcepts) {
            this.expectedConcepts = expectedConcepts;
        }

        public String getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(String difficulty) {
            this.difficulty = difficulty;
        }

        public List<String> getFollowUpQuestions() {
            return followUpQuestions;
        }

        public void setFollowUpQuestions(List<String> followUpQuestions) {
            this.followUpQuestions = followUpQuestions;
        }
    }

    public InterviewResponse() {
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

    public List<AIQuestionItem> getQuestions() {
        return questions;
    }

    public void setQuestions(List<AIQuestionItem> questions) {
        this.questions = questions;
    }
}
