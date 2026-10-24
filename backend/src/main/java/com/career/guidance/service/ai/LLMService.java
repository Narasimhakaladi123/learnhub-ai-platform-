package com.career.guidance.service.ai;

import com.career.guidance.dto.ai.*;
import com.career.guidance.dto.response.InterviewEvaluationDto;

public interface LLMService {

    CareerRecommendationResponse generateCareerRecommendation(CareerRecommendationRequest request);

    SkillGapResponse analyzeSkillGap(SkillGapRequest request);

    InterviewResponse generateInterviewQuestions(InterviewRequest request);

    InterviewEvaluationDto evaluateInterviewAnswer(String role, String question, String expectedConcepts, String userAnswer);

    String generateChatResponse(String studentContext, String conversationHistory, String userMessage);
}
