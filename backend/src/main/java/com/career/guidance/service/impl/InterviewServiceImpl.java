package com.career.guidance.service.impl;

import com.career.guidance.dto.ai.InterviewRequest;
import com.career.guidance.dto.ai.InterviewResponse;
import com.career.guidance.dto.request.EvaluateInterviewAnswerRequest;
import com.career.guidance.dto.request.GenerateInterviewRequest;
import com.career.guidance.dto.response.InterviewEvaluationDto;
import com.career.guidance.dto.response.InterviewQuestionDto;
import com.career.guidance.entity.*;
import com.career.guidance.entity.enums.AuditAction;
import com.career.guidance.entity.enums.NotificationType;
import com.career.guidance.exception.ResourceNotFoundException;
import com.career.guidance.repository.*;
import com.career.guidance.service.AuditLogService;
import com.career.guidance.service.InterviewService;
import com.career.guidance.service.NotificationService;
import com.career.guidance.service.ai.LLMService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InterviewServiceImpl implements InterviewService {

    private final InterviewSessionRepository sessionRepository;
    private final InterviewQuestionRepository questionRepository;
    private final StudentProfileRepository profileRepository;
    private final LLMService llmService;
    private final NotificationService notificationService;
    private final AuditLogService auditLogService;
    private final ObjectMapper objectMapper;

    public InterviewServiceImpl(
            InterviewSessionRepository sessionRepository,
            InterviewQuestionRepository questionRepository,
            StudentProfileRepository profileRepository,
            LLMService llmService,
            NotificationService notificationService,
            AuditLogService auditLogService) {
        this.sessionRepository = sessionRepository;
        this.questionRepository = questionRepository;
        this.profileRepository = profileRepository;
        this.llmService = llmService;
        this.notificationService = notificationService;
        this.auditLogService = auditLogService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    @Transactional
    public List<InterviewQuestionDto> startInterviewSession(User user, GenerateInterviewRequest request) {
        StudentProfile profile = profileRepository.findByUser(user)
            .orElseGet(() -> profileRepository.save(new StudentProfile(user)));

        String topicsStr = request.getTopics() != null ? String.join(", ", request.getTopics()) : "Core Fundamentals";
        InterviewSession session = new InterviewSession(profile, request.getRole(), request.getDifficulty(), topicsStr);
        session = sessionRepository.save(session);

        InterviewRequest aiReq = new InterviewRequest(
            request.getRole(),
            request.getDifficulty().name(),
            request.getTopics() != null ? request.getTopics() : List.of("General", "Architecture"),
            5
        );

        InterviewResponse aiResp = llmService.generateInterviewQuestions(aiReq);

        List<InterviewQuestion> questions = new ArrayList<>();
        for (InterviewResponse.AIQuestionItem item : aiResp.getQuestions()) {
            InterviewQuestion q = new InterviewQuestion(
                session,
                item.getQuestion(),
                writeJson(item.getExpectedConcepts()),
                item.getDifficulty(),
                writeJson(item.getFollowUpQuestions())
            );
            questions.add(q);
        }

        questions = questionRepository.saveAll(questions);
        session.setQuestions(questions);

        auditLogService.log(user.getEmail(), AuditAction.INTERVIEW_PRACTICE, "Generated interview session for role: " + request.getRole(), null);

        return questions.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InterviewEvaluationDto evaluateAnswer(User user, EvaluateInterviewAnswerRequest request) {
        InterviewQuestion question = questionRepository.findById(request.getQuestionId())
            .orElseThrow(() -> new ResourceNotFoundException("Interview question not found: " + request.getQuestionId()));

        InterviewEvaluationDto evaluation = llmService.evaluateInterviewAnswer(
            question.getSession().getTargetRole(),
            question.getQuestionText(),
            question.getExpectedConcepts(),
            request.getAnswer()
        );

        question.setUserAnswer(request.getAnswer());
        question.setStrengths(evaluation.getStrengths());
        question.setMissingConcepts(writeJson(evaluation.getMissingConcepts()));
        question.setSuggestedStructure(evaluation.getSuggestedStructure());
        question.setAreasToImprove(writeJson(evaluation.getAreasToImprove()));
        question.setEvaluationScore(evaluation.getScore());
        question.setFeedback(evaluation.getFeedback());
        questionRepository.save(question);

        evaluation.setQuestionId(question.getId());

        auditLogService.log(user.getEmail(), AuditAction.INTERVIEW_PRACTICE, "Evaluated interview answer for question ID: " + question.getId() + ", score: " + evaluation.getScore(), null);

        return evaluation;
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewQuestionDto> getSessionQuestions(User user, UUID sessionId) {
        InterviewSession session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new ResourceNotFoundException("Interview session not found: " + sessionId));

        return questionRepository.findBySession(session).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    private InterviewQuestionDto toDto(InterviewQuestion q) {
        InterviewQuestionDto dto = new InterviewQuestionDto();
        dto.setId(q.getId());
        dto.setSessionId(q.getSession().getId());
        dto.setQuestionText(q.getQuestionText());
        dto.setExpectedConcepts(readJsonList(q.getExpectedConcepts()));
        dto.setDifficulty(q.getDifficulty());
        dto.setFollowUpQuestions(readJsonList(q.getFollowUpQuestions()));
        dto.setUserAnswer(q.getUserAnswer());
        dto.setStrengths(q.getStrengths());
        dto.setMissingConcepts(q.getMissingConcepts());
        dto.setSuggestedStructure(q.getSuggestedStructure());
        dto.setAreasToImprove(q.getAreasToImprove());
        dto.setEvaluationScore(q.getEvaluationScore());
        return dto;
    }

    private String writeJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return "[]";
        }
    }

    private List<String> readJsonList(String json) {
        if (json == null || json.isBlank()) return Collections.emptyList();
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
