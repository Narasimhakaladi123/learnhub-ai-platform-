package com.career.guidance.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class CounselorNoteRequest {

    @NotNull(message = "Student profile ID is required")
    private UUID studentProfileId;

    @NotBlank(message = "Note is required")
    private String note;

    private String recommendedCareer;

    public CounselorNoteRequest() {
    }

    public CounselorNoteRequest(UUID studentProfileId, String note, String recommendedCareer) {
        this.studentProfileId = studentProfileId;
        this.note = note;
        this.recommendedCareer = recommendedCareer;
    }

    // Getters and Setters
    public UUID getStudentProfileId() {
        return studentProfileId;
    }

    public void setStudentProfileId(UUID studentProfileId) {
        this.studentProfileId = studentProfileId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRecommendedCareer() {
        return recommendedCareer;
    }

    public void setRecommendedCareer(String recommendedCareer) {
        this.recommendedCareer = recommendedCareer;
    }
}
