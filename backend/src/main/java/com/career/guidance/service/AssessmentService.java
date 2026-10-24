package com.career.guidance.service;

import com.career.guidance.dto.request.SubmitAssessmentRequest;
import com.career.guidance.dto.response.AssessmentDto;
import com.career.guidance.dto.response.AssessmentResultDto;
import com.career.guidance.entity.User;

import java.util.List;
import java.util.UUID;

public interface AssessmentService {
    List<AssessmentDto> getAllAssessments();
    AssessmentDto getAssessmentById(UUID id);
    AssessmentDto getAssessmentBySlug(String slug);
    AssessmentResultDto submitAssessment(User user, SubmitAssessmentRequest request);
    List<AssessmentResultDto> getUserAssessmentHistory(User user);
    AssessmentResultDto getAttemptResult(User user, UUID attemptId);
}
