package com.career.guidance.service.impl;

import com.career.guidance.dto.ai.CareerRecommendationRequest;
import com.career.guidance.dto.ai.CareerRecommendationResponse;
import com.career.guidance.dto.response.CareerRecommendationDto;
import com.career.guidance.entity.*;
import com.career.guidance.entity.enums.AuditAction;
import com.career.guidance.entity.enums.NotificationType;
import com.career.guidance.entity.enums.RequirementLevel;
import com.career.guidance.exception.ResourceNotFoundException;
import com.career.guidance.repository.*;
import com.career.guidance.service.*;
import com.career.guidance.service.ai.LLMService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CareerRecommendationServiceImpl implements CareerRecommendationService {

    private static final Logger log = LoggerFactory.getLogger(CareerRecommendationServiceImpl.class);

    private final StudentProfileRepository profileRepository;
    private final CareerPathRepository careerPathRepository;
    private final CareerSkillRepository careerSkillRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final StudentInterestRepository interestRepository;
    private final StudentProjectRepository projectRepository;
    private final AssessmentAttemptRepository attemptRepository;
    private final CareerRecommendationRepository recommendationRepository;
    private final LLMService llmService;
    private final NotificationService notificationService;
    private final AuditLogService auditLogService;
    private final ObjectMapper objectMapper;

    public CareerRecommendationServiceImpl(
            StudentProfileRepository profileRepository,
            CareerPathRepository careerPathRepository,
            CareerSkillRepository careerSkillRepository,
            StudentSkillRepository studentSkillRepository,
            StudentInterestRepository interestRepository,
            StudentProjectRepository projectRepository,
            AssessmentAttemptRepository attemptRepository,
            CareerRecommendationRepository recommendationRepository,
            LLMService llmService,
            NotificationService notificationService,
            AuditLogService auditLogService) {
        this.profileRepository = profileRepository;
        this.careerPathRepository = careerPathRepository;
        this.careerSkillRepository = careerSkillRepository;
        this.studentSkillRepository = studentSkillRepository;
        this.interestRepository = interestRepository;
        this.projectRepository = projectRepository;
        this.attemptRepository = attemptRepository;
        this.recommendationRepository = recommendationRepository;
        this.llmService = llmService;
        this.notificationService = notificationService;
        this.auditLogService = auditLogService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    @Transactional
    public List<CareerRecommendationDto> generateRecommendations(User user) {
        StudentProfile profile = profileRepository.findByUser(user)
            .orElseGet(() -> profileRepository.save(new StudentProfile(user)));

        log.info("Generating hybrid career recommendations for student: {}", user.getEmail());

        List<StudentSkill> studentSkills = studentSkillRepository.findByProfile(profile);
        Map<String, StudentSkill> studentSkillMap = studentSkills.stream()
            .collect(Collectors.toMap(ss -> ss.getSkill().getName().toLowerCase().trim(), ss -> ss, (a, b) -> a));

        List<StudentInterest> interests = interestRepository.findByProfile(profile);
        Set<String> interestSet = interests.stream()
            .map(i -> i.getInterestArea().name().toLowerCase())
            .collect(Collectors.toSet());

        List<AssessmentAttempt> attempts = attemptRepository.findByProfileOrderByAttemptedAtDesc(profile);
        Map<String, Integer> assessmentScores = attempts.stream()
            .collect(Collectors.toMap(
                a -> a.getAssessment().getCategory().toLowerCase(),
                AssessmentAttempt::getScore,
                (s1, s2) -> Math.max(s1, s2)
            ));

        List<CareerPath> allCareers = careerPathRepository.findAll();
        if (allCareers.isEmpty()) {
            return Collections.emptyList();
        }

        // 1. DETERMINISTIC LAYER: Calculate score for each career path
        Map<CareerPath, Integer> careerScores = new HashMap<>();
        Map<CareerPath, List<String>> matchingSkillsMap = new HashMap<>();
        Map<CareerPath, List<String>> missingSkillsMap = new HashMap<>();

        for (CareerPath career : allCareers) {
            List<CareerSkill> careerSkills = careerSkillRepository.findByCareerPath(career);
            int totalWeight = 0;
            double earnedWeight = 0;

            List<String> matching = new ArrayList<>();
            List<String> missing = new ArrayList<>();

            for (CareerSkill cs : careerSkills) {
                int weight = cs.getWeight();
                if (cs.getRequirementLevel() == RequirementLevel.REQUIRED) {
                    weight *= 2;
                }
                totalWeight += weight;

                String skillName = cs.getSkill().getName().toLowerCase().trim();
                if (studentSkillMap.containsKey(skillName)) {
                    StudentSkill ss = studentSkillMap.get(skillName);
                    matching.add(cs.getSkill().getName());

                    double proficiencyRatio = (double) ss.getProficiency().getLevelWeight() / cs.getMinimumProficiency().getLevelWeight();
                    earnedWeight += Math.min(1.2, proficiencyRatio) * weight;
                } else {
                    missing.add(cs.getSkill().getName());
                }
            }

            double baseScore = totalWeight > 0 ? (earnedWeight / totalWeight) * 75.0 : 40.0;

            // Interest bonus (+10%)
            if (career.getCategory() != null && interestSet.contains(career.getCategory().toLowerCase())) {
                baseScore += 10.0;
            }

            // Assessment performance bonus (+10%)
            String categoryLower = career.getCategory() != null ? career.getCategory().toLowerCase() : "";
            if (assessmentScores.containsKey(categoryLower) && assessmentScores.get(categoryLower) >= 70) {
                baseScore += 10.0;
            }

            // Project experience bonus (+5%)
            if (!projectRepository.findByProfile(profile).isEmpty()) {
                baseScore += 5.0;
            }

            int finalScore = (int) Math.round(Math.min(96, Math.max(25, baseScore)));
            careerScores.put(career, finalScore);
            matchingSkillsMap.put(career, matching);
            missingSkillsMap.put(career, missing);
        }

        // Sort careers by deterministic score descending
        List<Map.Entry<CareerPath, Integer>> sortedCareers = careerScores.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .limit(5)
            .collect(Collectors.toList());

        // Clear previous recommendations for this profile
        recommendationRepository.deleteByProfile(profile);

        List<CareerRecommendation> savedRecommendations = new ArrayList<>();

        for (Map.Entry<CareerPath, Integer> entry : sortedCareers) {
            CareerPath career = entry.getKey();
            int score = entry.getValue();
            List<String> matching = matchingSkillsMap.get(career);
            List<String> missing = missingSkillsMap.get(career);

            // 2. LLM LAYER: Generate personalized explanation and next steps
            CareerRecommendationRequest llmReq = new CareerRecommendationRequest();
            llmReq.setStudentName(user.getFullName());
            llmReq.setDegree(profile.getDegree() != null ? profile.getDegree() : "B.Tech");
            llmReq.setBranch(profile.getBranch() != null ? profile.getBranch() : "Computer Science");
            llmReq.setCgpa(profile.getCgpa());
            llmReq.setInterests(interests.stream().map(i -> i.getInterestArea().name()).collect(Collectors.toList()));
            llmReq.setSkillsWithProficiency(studentSkills.stream().collect(Collectors.toMap(s -> s.getSkill().getName(), s -> s.getProficiency().name(), (a, b) -> a)));
            llmReq.setAssessmentScores(assessmentScores);
            llmReq.setTargetCareerPreference(career.getName());

            CareerRecommendationResponse llmResp = llmService.generateCareerRecommendation(llmReq);

            CareerRecommendation rec = new CareerRecommendation();
            rec.setProfile(profile);
            rec.setCareerPath(career);
            rec.setCareerName(career.getName());
            rec.setCompatibilityScore(score);
            rec.setMatchingSkillsJson(writeJson(matching));
            rec.setMissingSkillsJson(writeJson(missing));
            rec.setExplanation(llmResp.getExplanation());
            rec.setNextStepsJson(writeJson(llmResp.getNextSteps()));
            rec.setTargetCareer(profile.getTargetCareer() != null && profile.getTargetCareer().equalsIgnoreCase(career.getName()));
            rec.setGeneratedAt(LocalDateTime.now());

            savedRecommendations.add(recommendationRepository.save(rec));
        }

        // Update overall readiness on student profile based on top match
        if (!savedRecommendations.isEmpty()) {
            profile.setReadinessPercentage(savedRecommendations.get(0).getCompatibilityScore());
            if (profile.getTargetCareer() == null) {
                profile.setTargetCareer(savedRecommendations.get(0).getCareerName());
            }
            profileRepository.save(profile);
        }

        notificationService.createNotification(
            user,
            "Career Recommendations Updated",
            "Your personalized career compatibility scores and learning pathways have been updated based on your latest profile data.",
            NotificationType.RECOMMENDATION
        );

        auditLogService.log(user.getEmail(), AuditAction.RECOMMENDATION_GENERATE, "Generated recommendations for " + savedRecommendations.size() + " careers", null);

        return savedRecommendations.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CareerRecommendationDto> getRecommendationsForUser(User user) {
        StudentProfile profile = profileRepository.findByUser(user)
            .orElseGet(() -> profileRepository.save(new StudentProfile(user)));

        List<CareerRecommendation> list = recommendationRepository.findByProfileOrderByCompatibilityScoreDesc(profile);
        if (list.isEmpty()) {
            return generateRecommendations(user);
        }
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CareerRecommendationDto setTargetCareer(User user, UUID careerId) {
        StudentProfile profile = profileRepository.findByUser(user)
            .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));

        CareerPath career = careerPathRepository.findById(careerId)
            .orElseThrow(() -> new ResourceNotFoundException("Career path not found: " + careerId));

        profile.setTargetCareer(career.getName());

        List<CareerRecommendation> recs = recommendationRepository.findByProfileOrderByCompatibilityScoreDesc(profile);
        CareerRecommendation targetRec = null;
        for (CareerRecommendation r : recs) {
            boolean isMatch = r.getCareerPath().getId().equals(careerId);
            r.setTargetCareer(isMatch);
            recommendationRepository.save(r);
            if (isMatch) targetRec = r;
        }

        if (targetRec != null) {
            profile.setReadinessPercentage(targetRec.getCompatibilityScore());
        }
        profileRepository.save(profile);

        notificationService.createNotification(
            user,
            "Target Career Updated",
            "You selected " + career.getName() + " as your primary target career.",
            NotificationType.MILESTONE
        );

        auditLogService.log(user.getEmail(), AuditAction.PROFILE_UPDATE, "Set target career to " + career.getName(), null);

        return targetRec != null ? toDto(targetRec) : null;
    }

    private CareerRecommendationDto toDto(CareerRecommendation r) {
        CareerRecommendationDto dto = new CareerRecommendationDto();
        dto.setId(r.getId());
        dto.setCareerId(r.getCareerPath().getId());
        dto.setCareerName(r.getCareerName());
        dto.setSlug(r.getCareerPath().getSlug());
        dto.setCompatibilityScore(r.getCompatibilityScore());
        dto.setMatchingSkills(readJsonList(r.getMatchingSkillsJson()));
        dto.setMissingSkills(readJsonList(r.getMissingSkillsJson()));
        dto.setExplanation(r.getExplanation());
        dto.setNextSteps(readJsonList(r.getNextStepsJson()));
        dto.setTargetCareer(r.isTargetCareer());
        dto.setGeneratedAt(r.getGeneratedAt());
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
