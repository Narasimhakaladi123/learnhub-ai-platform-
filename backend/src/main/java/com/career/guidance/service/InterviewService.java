package com.career.guidance.service;

import com.career.guidance.dto.request.EvaluateInterviewAnswerRequest;
import com.career.guidance.dto.request.GenerateInterviewRequest;
import com.career.guidance.dto.response.InterviewEvaluationDto;
import com.career.guidance.dto.response.InterviewQuestionDto;
import com.career.guidance.entity.User;

import java.util.List;
import java.util.UUID;

public interface InterviewService {
    List<InterviewQuestionDto> startInterviewSession(User user, GenerateInterviewRequest request);
    InterviewEvaluationDto evaluateAnswer(User user, EvaluateInterviewAnswerRequest request);
    List<InterviewQuestionDto> getSessionQuestions(User user, UUID sessionId);
}
