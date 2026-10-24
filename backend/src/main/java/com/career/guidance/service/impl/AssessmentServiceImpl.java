package com.career.guidance.service.impl;

import com.career.guidance.dto.request.SubmitAssessmentRequest;
import com.career.guidance.dto.response.AssessmentDto;
import com.career.guidance.dto.response.AssessmentQuestionDto;
import com.career.guidance.dto.response.AssessmentResultDto;
import com.career.guidance.entity.*;
import com.career.guidance.entity.enums.AuditAction;
import com.career.guidance.entity.enums.NotificationType;
import com.career.guidance.exception.BadRequestException;
import com.career.guidance.exception.ResourceNotFoundException;
import com.career.guidance.repository.*;
import com.career.guidance.service.AssessmentService;
import com.career.guidance.service.AuditLogService;
import com.career.guidance.service.NotificationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final QuestionRepository questionRepository;
    private final AssessmentAttemptRepository attemptRepository;
    private final AssessmentAnswerRepository answerRepository;
    private final StudentProfileRepository profileRepository;
    private final NotificationService notificationService;
    private final AuditLogService auditLogService;
    private final ObjectMapper objectMapper;

    public AssessmentServiceImpl(
            AssessmentRepository assessmentRepository,
            QuestionRepository questionRepository,
            AssessmentAttemptRepository attemptRepository,
            AssessmentAnswerRepository answerRepository,
            StudentProfileRepository profileRepository,
            NotificationService notificationService,
            AuditLogService auditLogService) {
        this.assessmentRepository = assessmentRepository;
        this.questionRepository = questionRepository;
        this.attemptRepository = attemptRepository;
        this.answerRepository = answerRepository;
        this.profileRepository = profileRepository;
        this.notificationService = notificationService;
        this.auditLogService = auditLogService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentDto> getAllAssessments() {
        return assessmentRepository.findAll().stream()
            .map(a -> toDto(a, false))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AssessmentDto getAssessmentById(UUID id) {
        Assessment assessment = assessmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Assessment not found: " + id));
        return toDto(assessment, true);
    }

    @Override
    @Transactional(readOnly = true)
    public AssessmentDto getAssessmentBySlug(String slug) {
        Assessment assessment = assessmentRepository.findBySlug(slug)
            .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with slug: " + slug));
        return toDto(assessment, true);
    }

    @Override
    @Transactional
    public AssessmentResultDto submitAssessment(User user, SubmitAssessmentRequest request) {
        StudentProfile profile = profileRepository.findByUser(user)
            .orElseGet(() -> profileRepository.save(new StudentProfile(user)));

        Assessment assessment = assessmentRepository.findById(request.getAssessmentId())
            .orElseThrow(() -> new ResourceNotFoundException("Assessment not found: " + request.getAssessmentId()));

        List<Question> questions = questionRepository.findByAssessment(assessment);
        if (questions.isEmpty()) {
            throw new BadRequestException("Assessment has no questions available for grading");
        }

        Map<UUID, String> submittedAnswers = request.getAnswers() != null ? request.getAnswers() : Collections.emptyMap();

        int correctCount = 0;
        int incorrectCount = 0;
        int totalEarnedPoints = 0;
        int maxPossiblePoints = 0;

        List<AssessmentAnswer> answersToSave = new ArrayList<>();
        List<AssessmentResultDto.QuestionFeedbackDto> feedbackList = new ArrayList<>();

        for (Question q : questions) {
            maxPossiblePoints += q.getPoints();
            String submitted = submittedAnswers.get(q.getId());
            boolean isCorrect = false;

            List<String> correctAnswers = parseJsonList(q.getCorrectAnswersJson());
            if (submitted != null && !submitted.isBlank()) {
                String cleanSubmitted = submitted.trim();
                for (String ca : correctAnswers) {
                    if (ca.equalsIgnoreCase(cleanSubmitted)) {
                        isCorrect = true;
                        break;
                    }
                }
            }

            int points = isCorrect ? q.getPoints() : 0;
            if (isCorrect) {
                correctCount++;
                totalEarnedPoints += points;
            } else {
                incorrectCount++;
            }

            AssessmentResultDto.QuestionFeedbackDto fb = new AssessmentResultDto.QuestionFeedbackDto();
            fb.setQuestionId(q.getId());
            fb.setQuestionText(q.getQuestionText());
            fb.setSelectedAnswer(submitted != null ? submitted : "Not Answered");
            fb.setCorrectAnswer(String.join(", ", correctAnswers));
            fb.setCorrect(isCorrect);
            fb.setExplanation(q.getExplanation());
            fb.setPointsAwarded(points);
            feedbackList.add(fb);
        }

        int percentageScore = maxPossiblePoints > 0 ? (int) Math.round(((double) totalEarnedPoints / maxPossiblePoints) * 100) : 0;
        boolean passed = percentageScore >= assessment.getPassingScore();

        AssessmentAttempt attempt = new AssessmentAttempt(
            profile,
            assessment,
            percentageScore,
            questions.size(),
            correctCount,
            incorrectCount,
            request.getTimeTakenSeconds(),
            passed
        );
        attempt = attemptRepository.save(attempt);

        for (Question q : questions) {
            String submitted = submittedAnswers.get(q.getId());
            boolean isCorrect = false;
            List<String> correctAnswers = parseJsonList(q.getCorrectAnswersJson());
            if (submitted != null) {
                for (String ca : correctAnswers) {
                    if (ca.equalsIgnoreCase(submitted.trim())) {
                        isCorrect = true;
                        break;
                    }
                }
            }
            int points = isCorrect ? q.getPoints() : 0;
            answersToSave.add(new AssessmentAnswer(attempt, q, submitted, isCorrect, points));
        }
        answerRepository.saveAll(answersToSave);

        notificationService.createNotification(
            user,
            passed ? "Assessment Passed!" : "Assessment Result",
            "You scored " + percentageScore + "% on the " + assessment.getTitle() + " assessment (" + (passed ? "Passed" : "Needs Review") + ").",
            NotificationType.ASSESSMENT
        );

        auditLogService.log(
            user.getEmail(),
            AuditAction.ASSESSMENT_SUBMIT,
            "Submitted assessment: " + assessment.getTitle() + ", score: " + percentageScore + "%",
            null
        );

        AssessmentResultDto result = new AssessmentResultDto();
        result.setId(attempt.getId());
        result.setAssessmentTitle(assessment.getTitle());
        result.setCategory(assessment.getCategory());
        result.setScore(percentageScore);
        result.setTotalQuestions(questions.size());
        result.setCorrectCount(correctCount);
        result.setIncorrectCount(incorrectCount);
        result.setTimeTakenSeconds(request.getTimeTakenSeconds());
        result.setPassed(passed);
        result.setAttemptedAt(attempt.getAttemptedAt());
        result.setAnswers(feedbackList);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentResultDto> getUserAssessmentHistory(User user) {
        StudentProfile profile = profileRepository.findByUser(user)
            .orElseGet(() -> profileRepository.save(new StudentProfile(user)));

        List<AssessmentAttempt> attempts = attemptRepository.findByProfileOrderByAttemptedAtDesc(profile);
        return attempts.stream().map(a -> {
            AssessmentResultDto res = new AssessmentResultDto();
            res.setId(a.getId());
            res.setAssessmentTitle(a.getAssessment().getTitle());
            res.setCategory(a.getAssessment().getCategory());
            res.setScore(a.getScore());
            res.setTotalQuestions(a.getTotalQuestions());
            res.setCorrectCount(a.getCorrectCount());
            res.setIncorrectCount(a.getIncorrectCount());
            res.setTimeTakenSeconds(a.getTimeTakenSeconds());
            res.setPassed(a.isPassed());
            res.setAttemptedAt(a.getAttemptedAt());
            return res;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AssessmentResultDto getAttemptResult(User user, UUID attemptId) {
        AssessmentAttempt attempt = attemptRepository.findById(attemptId)
            .orElseThrow(() -> new ResourceNotFoundException("Assessment attempt not found: " + attemptId));

        List<AssessmentAnswer> answers = answerRepository.findByAttempt(attempt);

        AssessmentResultDto result = new AssessmentResultDto();
        result.setId(attempt.getId());
        result.setAssessmentTitle(attempt.getAssessment().getTitle());
        result.setCategory(attempt.getAssessment().getCategory());
        result.setScore(attempt.getScore());
        result.setTotalQuestions(attempt.getTotalQuestions());
        result.setCorrectCount(attempt.getCorrectCount());
        result.setIncorrectCount(attempt.getIncorrectCount());
        result.setTimeTakenSeconds(attempt.getTimeTakenSeconds());
        result.setPassed(attempt.isPassed());
        result.setAttemptedAt(attempt.getAttemptedAt());

        List<AssessmentResultDto.QuestionFeedbackDto> feedbackList = answers.stream().map(ans -> {
            AssessmentResultDto.QuestionFeedbackDto fb = new AssessmentResultDto.QuestionFeedbackDto();
            fb.setQuestionId(ans.getQuestion().getId());
            fb.setQuestionText(ans.getQuestion().getQuestionText());
            fb.setSelectedAnswer(ans.getSelectedAnswers() != null ? ans.getSelectedAnswers() : "Not Answered");
            fb.setCorrectAnswer(String.join(", ", parseJsonList(ans.getQuestion().getCorrectAnswersJson())));
            fb.setCorrect(ans.isCorrect());
            fb.setExplanation(ans.getQuestion().getExplanation());
            fb.setPointsAwarded(ans.getPointsAwarded());
            return fb;
        }).collect(Collectors.toList());

        result.setAnswers(feedbackList);
        return result;
    }

    private AssessmentDto toDto(Assessment a, boolean includeQuestions) {
        AssessmentDto dto = new AssessmentDto();
        dto.setId(a.getId());
        dto.setTitle(a.getTitle());
        dto.setSlug(a.getSlug());
        dto.setCategory(a.getCategory());
        dto.setDifficulty(a.getDifficulty());
        dto.setTimeLimitMinutes(a.getTimeLimitMinutes());
        dto.setPassingScore(a.getPassingScore());
        dto.setDescription(a.getDescription());

        List<Question> questions = questionRepository.findByAssessment(a);
        dto.setTotalQuestions(questions.size());

        if (includeQuestions) {
            dto.setQuestions(questions.stream().map(q -> new AssessmentQuestionDto(
                q.getId(),
                q.getQuestionText(),
                q.getQuestionType(),
                parseJsonList(q.getOptionsJson()),
                q.getPoints()
            )).collect(Collectors.toList()));
        }
        return dto;
    }

    private List<String> parseJsonList(String json) {
        if (json == null || json.isBlank()) return Collections.emptyList();
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return List.of(json.split(","));
        }
    }
}
