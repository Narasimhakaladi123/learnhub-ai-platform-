package com.career.guidance.service.impl;

import com.career.guidance.dto.response.AdminAnalyticsDto;
import com.career.guidance.dto.response.UserProfileResponse;
import com.career.guidance.entity.StudentProfile;
import com.career.guidance.entity.User;
import com.career.guidance.entity.enums.AuditAction;
import com.career.guidance.entity.enums.RoleType;
import com.career.guidance.exception.ResourceNotFoundException;
import com.career.guidance.repository.*;
import com.career.guidance.service.AdminService;
import com.career.guidance.service.AuditLogService;
import com.career.guidance.service.StudentProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final StudentProfileRepository profileRepository;
    private final AssessmentAttemptRepository attemptRepository;
    private final LearningRoadmapRepository roadmapRepository;
    private final ResumeRepository resumeRepository;
    private final SkillGapRepository skillGapRepository;
    private final StudentProfileService studentProfileService;
    private final AuditLogService auditLogService;

    public AdminServiceImpl(
            UserRepository userRepository,
            StudentProfileRepository profileRepository,
            AssessmentAttemptRepository attemptRepository,
            LearningRoadmapRepository roadmapRepository,
            ResumeRepository resumeRepository,
            SkillGapRepository skillGapRepository,
            StudentProfileService studentProfileService,
            AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.attemptRepository = attemptRepository;
        this.roadmapRepository = roadmapRepository;
        this.resumeRepository = resumeRepository;
        this.skillGapRepository = skillGapRepository;
        this.studentProfileService = studentProfileService;
        this.auditLogService = auditLogService;
    }

    @Override
    @Transactional(readOnly = true)
    public AdminAnalyticsDto getAnalytics() {
        AdminAnalyticsDto dto = new AdminAnalyticsDto();
        dto.setTotalStudents(userRepository.countByRole(RoleType.ROLE_STUDENT));
        dto.setTotalCounselors(userRepository.countByRole(RoleType.ROLE_COUNSELOR));
        dto.setTotalAdmins(userRepository.countByRole(RoleType.ROLE_ADMIN));
        dto.setTotalAssessmentsTaken(attemptRepository.count());
        dto.setTotalRoadmapsCreated(roadmapRepository.count());
        dto.setTotalResumesAnalyzed(resumeRepository.count());

        List<StudentProfile> allProfiles = profileRepository.findAll();
        double avgReadiness = allProfiles.stream()
            .filter(p -> p.getReadinessPercentage() != null)
            .mapToInt(StudentProfile::getReadinessPercentage)
            .average().orElse(0.0);
        dto.setAverageReadinessScore(Math.round(avgReadiness * 10.0) / 10.0);

        // Top target careers
        Map<String, Long> targetCareers = allProfiles.stream()
            .filter(p -> p.getTargetCareer() != null && !p.getTargetCareer().isBlank())
            .collect(Collectors.groupingBy(StudentProfile::getTargetCareer, Collectors.counting()));
        dto.setTopTargetCareers(targetCareers);

        // Most common skill gaps
        Map<String, Long> commonGaps = skillGapRepository.findAll().stream()
            .collect(Collectors.groupingBy(g -> g.getSkillName(), Collectors.counting()));
        dto.setMostCommonSkillGaps(commonGaps);

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileResponse> getAllStudents() {
        List<User> students = userRepository.findByRole(RoleType.ROLE_STUDENT);
        return students.stream()
            .map(studentProfileService::getProfile)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User updateUserRole(UUID userId, RoleType newRole) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        user.setRole(newRole);
        user = userRepository.save(user);

        auditLogService.log("ADMIN", AuditAction.ADMIN_ACTION, "Updated user " + user.getEmail() + " role to " + newRole, null);
        return user;
    }

    @Override
    @Transactional
    public void toggleUserStatus(UUID userId, boolean enabled) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        user.setEnabled(enabled);
        userRepository.save(user);

        auditLogService.log("ADMIN", AuditAction.ADMIN_ACTION, (enabled ? "Enabled" : "Disabled") + " user: " + user.getEmail(), null);
    }
}
