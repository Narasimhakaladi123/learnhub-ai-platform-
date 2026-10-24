package com.career.guidance.service.impl;

import com.career.guidance.dto.request.*;
import com.career.guidance.dto.response.*;
import com.career.guidance.entity.*;
import com.career.guidance.entity.enums.AuditAction;
import com.career.guidance.entity.enums.InterestArea;
import com.career.guidance.entity.enums.SkillCategory;
import com.career.guidance.exception.BadRequestException;
import com.career.guidance.exception.ResourceNotFoundException;
import com.career.guidance.repository.*;
import com.career.guidance.service.AuditLogService;
import com.career.guidance.service.StudentProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentProfileServiceImpl implements StudentProfileService {

    private final UserRepository userRepository;
    private final StudentProfileRepository profileRepository;
    private final SkillRepository skillRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final StudentInterestRepository interestRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;
    private final InternshipRepository internshipRepository;
    private final StudentProjectRepository studentProjectRepository;
    private final CertificationRepository certificationRepository;
    private final CareerRecommendationRepository recommendationRepository;
    private final SkillGapRepository skillGapRepository;
    private final LearningRoadmapRepository roadmapRepository;
    private final RoadmapItemRepository roadmapItemRepository;
    private final AssessmentAttemptRepository attemptRepository;
    private final ProjectRepository projectRepository;
    private final AuditLogService auditLogService;

    public StudentProfileServiceImpl(
            UserRepository userRepository,
            StudentProfileRepository profileRepository,
            SkillRepository skillRepository,
            StudentSkillRepository studentSkillRepository,
            StudentInterestRepository interestRepository,
            EducationRepository educationRepository,
            ExperienceRepository experienceRepository,
            InternshipRepository internshipRepository,
            StudentProjectRepository studentProjectRepository,
            CertificationRepository certificationRepository,
            CareerRecommendationRepository recommendationRepository,
            SkillGapRepository skillGapRepository,
            LearningRoadmapRepository roadmapRepository,
            RoadmapItemRepository roadmapItemRepository,
            AssessmentAttemptRepository attemptRepository,
            ProjectRepository projectRepository,
            AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.skillRepository = skillRepository;
        this.studentSkillRepository = studentSkillRepository;
        this.interestRepository = interestRepository;
        this.educationRepository = educationRepository;
        this.experienceRepository = experienceRepository;
        this.internshipRepository = internshipRepository;
        this.studentProjectRepository = studentProjectRepository;
        this.certificationRepository = certificationRepository;
        this.recommendationRepository = recommendationRepository;
        this.skillGapRepository = skillGapRepository;
        this.roadmapRepository = roadmapRepository;
        this.roadmapItemRepository = roadmapItemRepository;
        this.attemptRepository = attemptRepository;
        this.projectRepository = projectRepository;
        this.auditLogService = auditLogService;
    }

    private StudentProfile getOrCreateProfile(User user) {
        return profileRepository.findByUser(user)
            .orElseGet(() -> profileRepository.save(new StudentProfile(user)));
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(User user) {
        StudentProfile profile = getOrCreateProfile(user);
        return buildProfileResponse(user, profile);
    }

    @Override
    @Transactional
    public UserProfileResponse updateProfile(User user, UpdateProfileRequest request) {
        StudentProfile profile = getOrCreateProfile(user);

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName().trim());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone().trim());
        }
        userRepository.save(user);

        if (request.getDegree() != null) profile.setDegree(request.getDegree());
        if (request.getBranch() != null) profile.setBranch(request.getBranch());
        if (request.getCollege() != null) profile.setCollege(request.getCollege());
        if (request.getGraduationYear() != null) profile.setGraduationYear(request.getGraduationYear());
        if (request.getCgpa() != null) profile.setCgpa(request.getCgpa());
        if (request.getTargetCareer() != null) profile.setTargetCareer(request.getTargetCareer());
        if (request.getBio() != null) profile.setBio(request.getBio());
        if (request.getLocation() != null) profile.setLocation(request.getLocation());

        profile = profileRepository.save(profile);
        auditLogService.log(user.getEmail(), AuditAction.PROFILE_UPDATE, "Updated profile personal details", null);

        return buildProfileResponse(user, profile);
    }

    @Override
    @Transactional
    public UserProfileResponse addSkill(User user, AddSkillRequest request) {
        StudentProfile profile = getOrCreateProfile(user);

        Skill skill;
        if (request.getSkillId() != null) {
            skill = skillRepository.findById(request.getSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found: " + request.getSkillId()));
        } else {
            skill = skillRepository.findByNameIgnoreCase(request.getSkillName().trim())
                .orElseGet(() -> {
                    SkillCategory cat = SkillCategory.GENERAL;
                    if (request.getCategory() != null) {
                        try {
                            cat = SkillCategory.valueOf(request.getCategory().toUpperCase());
                        } catch (Exception ignored) {
                        }
                    }
                    return skillRepository.save(new Skill(request.getSkillName().trim(), cat, "User-added technical skill"));
                });
        }

        Optional<StudentSkill> existing = studentSkillRepository.findByProfileAndSkill(profile, skill);
        if (existing.isPresent()) {
            StudentSkill ss = existing.get();
            ss.setProficiency(request.getProficiency());
            ss.setExperienceMonths(request.getExperienceMonths());
            studentSkillRepository.save(ss);
        } else {
            StudentSkill ss = new StudentSkill(profile, skill, request.getProficiency(), request.getExperienceMonths());
            studentSkillRepository.save(ss);
        }

        recalculateReadiness(profile);
        auditLogService.log(user.getEmail(), AuditAction.PROFILE_UPDATE, "Added/Updated skill: " + skill.getName(), null);
        return buildProfileResponse(user, profile);
    }

    @Override
    @Transactional
    public UserProfileResponse removeSkill(User user, UUID skillId) {
        StudentProfile profile = getOrCreateProfile(user);
        studentSkillRepository.deleteByProfileAndSkillId(profile, skillId);
        recalculateReadiness(profile);
        auditLogService.log(user.getEmail(), AuditAction.PROFILE_UPDATE, "Removed skill ID: " + skillId, null);
        return buildProfileResponse(user, profile);
    }

    @Override
    @Transactional
    public UserProfileResponse addEducation(User user, AddEducationRequest request) {
        StudentProfile profile = getOrCreateProfile(user);
        Education education = new Education(profile, request.getDegree(), request.getBranch(), request.getCollege(), request.getGraduationYear(), request.getCgpa());
        education.setStartYear(request.getStartYear());
        education.setEndYear(request.getEndYear());
        educationRepository.save(education);

        if (profile.getDegree() == null) {
            profile.setDegree(request.getDegree());
            profile.setBranch(request.getBranch());
            profile.setCollege(request.getCollege());
            profile.setGraduationYear(request.getGraduationYear());
            profile.setCgpa(request.getCgpa());
            profileRepository.save(profile);
        }
        return buildProfileResponse(user, profile);
    }

    @Override
    @Transactional
    public UserProfileResponse addExperience(User user, AddExperienceRequest request) {
        StudentProfile profile = getOrCreateProfile(user);
        Experience exp = new Experience(profile, request.getCompany(), request.getRole(), request.getStartDate(), request.getEndDate(), request.isCurrentJob(), request.getDescription());
        experienceRepository.save(exp);
        return buildProfileResponse(user, profile);
    }

    @Override
    @Transactional
    public UserProfileResponse addInternship(User user, AddInternshipRequest request) {
        StudentProfile profile = getOrCreateProfile(user);
        Internship internship = new Internship(profile, request.getCompany(), request.getRole(), request.getDuration(), request.getTechnologies(), request.getDescription());
        internshipRepository.save(internship);
        return buildProfileResponse(user, profile);
    }

    @Override
    @Transactional
    public UserProfileResponse addProject(User user, AddProjectRequest request) {
        StudentProfile profile = getOrCreateProfile(user);
        StudentProject project = new StudentProject(profile, request.getProjectName(), request.getDescription(), request.getTechnologies(), request.getGithubUrl(), request.getLiveUrl(), request.getRole());
        studentProjectRepository.save(project);
        return buildProfileResponse(user, profile);
    }

    @Override
    @Transactional
    public UserProfileResponse addCertification(User user, AddCertificationRequest request) {
        StudentProfile profile = getOrCreateProfile(user);
        Certification cert = new Certification(profile, request.getCertificateName(), request.getIssuingOrganization(), request.getIssueDate(), request.getCredentialUrl());
        certificationRepository.save(cert);
        return buildProfileResponse(user, profile);
    }

    @Override
    @Transactional
    public UserProfileResponse updateInterests(User user, UpdateInterestsRequest request) {
        StudentProfile profile = getOrCreateProfile(user);
        interestRepository.deleteByProfile(profile);

        for (InterestArea area : request.getInterests()) {
            interestRepository.save(new StudentInterest(profile, area));
        }

        return buildProfileResponse(user, profile);
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardSummaryDto getDashboardSummary(User user) {
        StudentProfile profile = getOrCreateProfile(user);
        DashboardSummaryDto summary = new DashboardSummaryDto();

        summary.setReadinessPercentage(profile.getReadinessPercentage() != null ? profile.getReadinessPercentage() : 0);
        summary.setTargetCareer(profile.getTargetCareer() != null ? profile.getTargetCareer() : "Java Backend Developer");

        // Skill progress bars
        List<StudentSkill> skills = studentSkillRepository.findByProfile(profile);
        Map<String, Integer> progressMap = new LinkedHashMap<>();
        for (StudentSkill ss : skills) {
            int percentage = switch (ss.getProficiency()) {
                case EXPERT -> 95;
                case ADVANCED -> 80;
                case INTERMEDIATE -> 60;
                case BEGINNER -> 35;
            };
            progressMap.put(ss.getSkill().getName(), percentage);
        }
        if (progressMap.isEmpty()) {
            progressMap.put("Java", 75);
            progressMap.put("SQL", 70);
            progressMap.put("Spring Boot", 60);
            progressMap.put("Docker", 30);
        }
        summary.setSkillProgress(progressMap);

        // Top recommendations
        List<CareerRecommendation> recs = recommendationRepository.findByProfileOrderByCompatibilityScoreDesc(profile);
        summary.setTopRecommendations(recs.stream().limit(3).map(this::toRecommendationDto).collect(Collectors.toList()));

        // Skill gaps
        List<SkillGap> gaps = skillGapRepository.findByProfileOrderBySequenceOrderAsc(profile);
        summary.setSkillGaps(gaps.stream().limit(5).map(this::toSkillGapDto).collect(Collectors.toList()));

        // Active roadmap
        Optional<LearningRoadmap> roadmapOpt = roadmapRepository.findFirstByProfileOrderByUpdatedAtDesc(profile);
        roadmapOpt.ifPresent(rm -> {
            RoadmapDto rDto = new RoadmapDto();
            rDto.setId(rm.getId());
            rDto.setCareerName(rm.getCareerName());
            rDto.setTotalPhases(rm.getTotalPhases());
            rDto.setCompletedPhases(rm.getCompletedPhases());
            rDto.setProgressPercentage(rm.getProgressPercentage());
            rDto.setUpdatedAt(rm.getUpdatedAt());

            List<RoadmapItem> items = roadmapItemRepository.findByRoadmapOrderBySequenceOrderAsc(rm);
            rDto.setItems(items.stream().map(this::toRoadmapItemDto).collect(Collectors.toList()));
            summary.setActiveRoadmap(rDto);

            summary.setUpcomingLearningTasks(items.stream()
                .filter(i -> i.getStatus() != com.career.guidance.entity.enums.RoadmapStatus.COMPLETED)
                .limit(3)
                .map(this::toRoadmapItemDto)
                .collect(Collectors.toList()));
        });

        // Recent assessment results
        List<AssessmentAttempt> attempts = attemptRepository.findByProfileOrderByAttemptedAtDesc(profile);
        summary.setRecentAssessments(attempts.stream().limit(3).map(a -> {
            AssessmentResultDto res = new AssessmentResultDto();
            res.setId(a.getId());
            res.setAssessmentTitle(a.getAssessment().getTitle());
            res.setCategory(a.getAssessment().getCategory());
            res.setScore(a.getScore());
            res.setPassed(a.isPassed());
            res.setAttemptedAt(a.getAttemptedAt());
            return res;
        }).collect(Collectors.toList()));

        // Recommended projects
        String slug = profile.getTargetCareer() != null
            ? profile.getTargetCareer().toLowerCase().replace(" ", "-")
            : "java-backend-developer";
        List<Project> projects = projectRepository.findByTargetCareerSlug(slug);
        if (projects.isEmpty()) {
            projects = projectRepository.findAll();
        }
        summary.setRecommendedProjects(projects.stream().limit(3).map(this::toProjectDto).collect(Collectors.toList()));

        summary.setRecentAiAdvice(
            "Your profile indicates a strong foundation in core software fundamentals. "
            + "Closing your skill gaps in containerization (Docker) and microservices architecture will improve your market readiness."
        );

        return summary;
    }

    private void recalculateReadiness(StudentProfile profile) {
        List<StudentSkill> skills = studentSkillRepository.findByProfile(profile);
        int totalWeight = 0;
        for (StudentSkill ss : skills) {
            totalWeight += ss.getProficiency().getLevelWeight() * 10;
        }
        int calculated = Math.min(95, Math.max(30, totalWeight));
        profile.setReadinessPercentage(calculated);
        profileRepository.save(profile);
    }

    private UserProfileResponse buildProfileResponse(User user, StudentProfile profile) {
        UserProfileResponse resp = new UserProfileResponse();
        resp.setUserId(user.getId());
        resp.setEmail(user.getEmail());
        resp.setFullName(user.getFullName());
        resp.setPhone(user.getPhone());
        resp.setRole(user.getRole());
        resp.setCreatedAt(user.getCreatedAt());

        resp.setProfileId(profile.getId());
        resp.setDegree(profile.getDegree());
        resp.setBranch(profile.getBranch());
        resp.setCollege(profile.getCollege());
        resp.setGraduationYear(profile.getGraduationYear());
        resp.setCgpa(profile.getCgpa());
        resp.setTargetCareer(profile.getTargetCareer());
        resp.setReadinessPercentage(profile.getReadinessPercentage());
        resp.setBio(profile.getBio());
        resp.setLocation(profile.getLocation());

        // Skills
        List<StudentSkill> studentSkills = studentSkillRepository.findByProfile(profile);
        resp.setSkills(studentSkills.stream()
            .map(ss -> new StudentSkillDto(
                ss.getId(),
                ss.getSkill().getId(),
                ss.getSkill().getName(),
                ss.getSkill().getCategory(),
                ss.getProficiency(),
                ss.getExperienceMonths()
            ))
            .collect(Collectors.toList()));

        // Interests
        List<StudentInterest> interests = interestRepository.findByProfile(profile);
        resp.setInterests(interests.stream().map(StudentInterest::getInterestArea).collect(Collectors.toList()));

        // Educations, Experiences, Internships, Projects, Certifications
        resp.setEducations(new ArrayList<>(educationRepository.findByProfile(profile)));
        resp.setExperiences(new ArrayList<>(experienceRepository.findByProfile(profile)));
        resp.setInternships(new ArrayList<>(internshipRepository.findByProfile(profile)));
        resp.setProjects(new ArrayList<>(studentProjectRepository.findByProfile(profile)));
        resp.setCertifications(new ArrayList<>(certificationRepository.findByProfile(profile)));

        return resp;
    }

    private CareerRecommendationDto toRecommendationDto(CareerRecommendation cr) {
        CareerRecommendationDto dto = new CareerRecommendationDto();
        dto.setId(cr.getId());
        dto.setCareerId(cr.getCareerPath().getId());
        dto.setCareerName(cr.getCareerName());
        dto.setSlug(cr.getCareerPath().getSlug());
        dto.setCompatibilityScore(cr.getCompatibilityScore());
        dto.setExplanation(cr.getExplanation());
        dto.setTargetCareer(cr.isTargetCareer());
        dto.setGeneratedAt(cr.getGeneratedAt());
        return dto;
    }

    private SkillGapDto toSkillGapDto(SkillGap gap) {
        return new SkillGapDto(
            gap.getSkill().getId(),
            gap.getSkillName(),
            gap.getCurrentProficiency(),
            gap.getRequiredProficiency(),
            gap.getPriorityLevel(),
            gap.getSequenceOrder()
        );
    }

    private RoadmapItemDto toRoadmapItemDto(RoadmapItem item) {
        RoadmapItemDto dto = new RoadmapItemDto();
        dto.setId(item.getId());
        dto.setPhaseNumber(item.getPhaseNumber());
        dto.setPhaseTitle(item.getPhaseTitle());
        dto.setTopic(item.getTopic());
        dto.setDescription(item.getDescription());
        dto.setDifficulty(item.getDifficulty());
        dto.setEstimatedDuration(item.getEstimatedDuration());
        dto.setStatus(item.getStatus());
        dto.setSequenceOrder(item.getSequenceOrder());
        return dto;
    }

    private ProjectDto toProjectDto(Project p) {
        ProjectDto dto = new ProjectDto();
        dto.setId(p.getId());
        dto.setTitle(p.getTitle());
        dto.setDescription(p.getDescription());
        dto.setDifficulty(p.getDifficulty());
        dto.setTechnologies(List.of(p.getTechnologies().split(",")));
        dto.setEstimatedDurationWeeks(p.getEstimatedDurationWeeks());
        dto.setTargetCareerSlug(p.getTargetCareerSlug());
        return dto;
    }
}
