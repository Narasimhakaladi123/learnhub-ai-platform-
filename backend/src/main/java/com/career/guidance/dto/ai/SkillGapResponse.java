package com.career.guidance.dto.ai;

import java.util.List;

public class SkillGapResponse {
    private String career;
    private List<String> matchingSkills;
    private List<String> missingSkills;
    private List<String> weakSkills;
    private List<PrioritizedLearningTopic> prioritizedSequence;
    private String summary;

    public static class PrioritizedLearningTopic {
        private String topic;
        private String priority; // Priority 1, Priority 2, Priority 3
        private String estimatedHours;
        private String reason;

        public PrioritizedLearningTopic() {
        }

        public PrioritizedLearningTopic(String topic, String priority, String estimatedHours, String reason) {
            this.topic = topic;
            this.priority = priority;
            this.estimatedHours = estimatedHours;
            this.reason = reason;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        public String getEstimatedHours() {
            return estimatedHours;
        }

        public void setEstimatedHours(String estimatedHours) {
            this.estimatedHours = estimatedHours;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }

    public SkillGapResponse() {
    }

    // Getters and Setters
    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public List<String> getMatchingSkills() {
        return matchingSkills;
    }

    public void setMatchingSkills(List<String> matchingSkills) {
        this.matchingSkills = matchingSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

    public List<String> getWeakSkills() {
        return weakSkills;
    }

    public void setWeakSkills(List<String> weakSkills) {
        this.weakSkills = weakSkills;
    }

    public List<PrioritizedLearningTopic> getPrioritizedSequence() {
        return prioritizedSequence;
    }

    public void setPrioritizedSequence(List<PrioritizedLearningTopic> prioritizedSequence) {
        this.prioritizedSequence = prioritizedSequence;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
