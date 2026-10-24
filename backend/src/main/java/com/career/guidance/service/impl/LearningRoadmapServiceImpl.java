package com.career.guidance.service.impl;

import com.career.guidance.dto.response.RoadmapDto;
import com.career.guidance.dto.response.RoadmapItemDto;
import com.career.guidance.entity.*;
import com.career.guidance.entity.enums.DifficultyLevel;
import com.career.guidance.entity.enums.NotificationType;
import com.career.guidance.entity.enums.RoadmapStatus;
import com.career.guidance.exception.ResourceNotFoundException;
import com.career.guidance.repository.*;
import com.career.guidance.service.LearningRoadmapService;
import com.career.guidance.service.NotificationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LearningRoadmapServiceImpl implements LearningRoadmapService {

    private final StudentProfileRepository profileRepository;
    private final CareerPathRepository careerPathRepository;
    private final SkillGapRepository skillGapRepository;
    private final LearningRoadmapRepository roadmapRepository;
    private final RoadmapItemRepository roadmapItemRepository;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public LearningRoadmapServiceImpl(
            StudentProfileRepository profileRepository,
            CareerPathRepository careerPathRepository,
            SkillGapRepository skillGapRepository,
            LearningRoadmapRepository roadmapRepository,
            RoadmapItemRepository roadmapItemRepository,
            NotificationService notificationService) {
        this.profileRepository = profileRepository;
        this.careerPathRepository = careerPathRepository;
        this.skillGapRepository = skillGapRepository;
        this.roadmapRepository = roadmapRepository;
        this.roadmapItemRepository = roadmapItemRepository;
        this.notificationService = notificationService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    @Transactional
    public RoadmapDto generateRoadmap(User user, UUID careerId) {
        StudentProfile profile = profileRepository.findByUser(user)
            .orElseGet(() -> profileRepository.save(new StudentProfile(user)));

        CareerPath career = careerPathRepository.findById(careerId)
            .orElseThrow(() -> new ResourceNotFoundException("Career path not found: " + careerId));

        // Find or create roadmap
        Optional<LearningRoadmap> existing = roadmapRepository.findByProfileAndCareerPath(profile, career);
        LearningRoadmap roadmap;
        if (existing.isPresent()) {
            roadmap = existing.get();
            roadmapItemRepository.deleteAll(roadmap.getItems());
            roadmap.getItems().clear();
        } else {
            roadmap = new LearningRoadmap(profile, career, career.getName());
            roadmap = roadmapRepository.save(roadmap);
        }

        // Fetch skill gaps for this career
        List<SkillGap> gaps = skillGapRepository.findByProfileAndCareerPathOrderBySequenceOrderAsc(profile, career);

        List<RoadmapItem> items = new ArrayList<>();
        int phaseNumber = 1;

        if (gaps.isEmpty()) {
            // General progression roadmap
            items.add(createItem(roadmap, phaseNumber++, "Core Foundations", "Fundamental Architecture & Principles",
                "Deep dive into design patterns and algorithmic optimization", DifficultyLevel.BEGINNER, "2 weeks",
                List.of("Computer Science Fundamentals"), List.of("Official Documentation", "Baeldung Tutorials"),
                List.of("Refactor legacy methods using clean code principles"), "Clean Code Repository"));

            items.add(createItem(roadmap, phaseNumber++, "Production Frameworks", career.getName() + " Modern Frameworks",
                "Building resilient applications with enterprise frameworks", DifficultyLevel.INTERMEDIATE, "3 weeks",
                List.of("Object-Oriented Programming"), List.of("Spring/Angular Guides", "Interactive Labs"),
                List.of("Build secure stateless RESTful API endpoints"), "E-Commerce REST API"));

            items.add(createItem(roadmap, phaseNumber++, "Containerization & Cloud", "Docker & Cloud Deployment",
                "Packaging applications and running in cloud native environments", DifficultyLevel.ADVANCED, "3 weeks",
                List.of("REST API Development"), List.of("Docker Docs", "AWS Architecture Center"),
                List.of("Create Dockerfile and multi-stage container build"), "Containerized Cloud Deployment"));
        } else {
            for (SkillGap gap : gaps) {
                String skillName = gap.getSkillName();
                DifficultyLevel diff = gap.getRequiredProficiency() == com.career.guidance.entity.enums.ProficiencyLevel.EXPERT
                    ? DifficultyLevel.ADVANCED
                    : (gap.getRequiredProficiency() == com.career.guidance.entity.enums.ProficiencyLevel.ADVANCED ? DifficultyLevel.ADVANCED : DifficultyLevel.INTERMEDIATE);

                items.add(createItem(
                    roadmap,
                    phaseNumber++,
                    "Phase " + (phaseNumber - 1) + ": " + skillName,
                    "Mastering " + skillName,
                    "Comprehensive hands-on curriculum covering " + skillName + " production patterns and operational best practices.",
                    diff,
                    "2-3 weeks",
                    List.of("Core programming fundamentals"),
                    List.of(skillName + " Official Docs", "Industry Best Practices Guide", "Hands-on Exercises"),
                    List.of("Implement practical test cases", "Build production module using " + skillName),
                    skillName + " Portfolio Showcase Project"
                ));
            }
        }

        roadmapItemRepository.saveAll(items);
        roadmap.setItems(items);
        roadmap.setTotalPhases(items.size());
        roadmap.setCompletedPhases(0);
        roadmap.setProgressPercentage(0);
        roadmap = roadmapRepository.save(roadmap);

        notificationService.createNotification(
            user,
            "Personalized Roadmap Generated",
            "A " + items.size() + "-phase structured learning roadmap for " + career.getName() + " has been generated for you.",
            NotificationType.ROADMAP
        );

        return toDto(roadmap);
    }

    @Override
    @Transactional
    public RoadmapDto getActiveRoadmap(User user) {
        StudentProfile profile = profileRepository.findByUser(user)
            .orElseGet(() -> profileRepository.save(new StudentProfile(user)));

        Optional<LearningRoadmap> active = roadmapRepository.findFirstByProfileOrderByUpdatedAtDesc(profile);
        if (active.isPresent()) {
            return toDto(active.get());
        }

        // Auto-generate for target career or first available career
        List<CareerPath> careers = careerPathRepository.findAll();
        if (!careers.isEmpty()) {
            CareerPath target = careers.get(0);
            if (profile.getTargetCareer() != null) {
                target = careerPathRepository.findByNameIgnoreCase(profile.getTargetCareer()).orElse(careers.get(0));
            }
            return generateRoadmap(user, target.getId());
        }

        return null;
    }

    @Override
    @Transactional
    public RoadmapItemDto updateItemStatus(User user, UUID itemId, RoadmapStatus status) {
        RoadmapItem item = roadmapItemRepository.findById(itemId)
            .orElseThrow(() -> new ResourceNotFoundException("Roadmap item not found: " + itemId));

        item.setStatus(status);
        item = roadmapItemRepository.save(item);

        LearningRoadmap roadmap = item.getRoadmap();
        roadmap.recalculateProgress();
        roadmapRepository.save(roadmap);

        if (status == RoadmapStatus.COMPLETED) {
            notificationService.createNotification(
                user,
                "Roadmap Phase Completed!",
                "Congratulations! You completed: " + item.getTopic() + ". Overall roadmap progress: " + roadmap.getProgressPercentage() + "%.",
                NotificationType.MILESTONE
            );
        }

        return toItemDto(item);
    }

    private RoadmapItem createItem(LearningRoadmap roadmap, int phaseNumber, String title, String topic, String desc,
                                   DifficultyLevel difficulty, String duration, List<String> prereqs, List<String> resources,
                                   List<String> tasks, String project) {
        RoadmapItem item = new RoadmapItem();
        item.setRoadmap(roadmap);
        item.setPhaseNumber(phaseNumber);
        item.setPhaseTitle(title);
        item.setTopic(topic);
        item.setDescription(desc);
        item.setDifficulty(difficulty);
        item.setEstimatedDuration(duration);
        item.setPrerequisites(writeJson(prereqs));
        item.setResources(writeJson(resources));
        item.setPracticeTasks(writeJson(tasks));
        item.setProjectRecommendation(project);
        item.setStatus(RoadmapStatus.NOT_STARTED);
        item.setSequenceOrder(phaseNumber);
        return item;
    }

    private RoadmapDto toDto(LearningRoadmap rm) {
        RoadmapDto dto = new RoadmapDto();
        dto.setId(rm.getId());
        dto.setCareerName(rm.getCareerName());
        dto.setTotalPhases(rm.getTotalPhases());
        dto.setCompletedPhases(rm.getCompletedPhases());
        dto.setProgressPercentage(rm.getProgressPercentage());
        dto.setUpdatedAt(rm.getUpdatedAt());

        List<RoadmapItem> items = roadmapItemRepository.findByRoadmapOrderBySequenceOrderAsc(rm);
        dto.setItems(items.stream().map(this::toItemDto).collect(Collectors.toList()));
        return dto;
    }

    private RoadmapItemDto toItemDto(RoadmapItem item) {
        RoadmapItemDto dto = new RoadmapItemDto();
        dto.setId(item.getId());
        dto.setPhaseNumber(item.getPhaseNumber());
        dto.setPhaseTitle(item.getPhaseTitle());
        dto.setTopic(item.getTopic());
        dto.setDescription(item.getDescription());
        dto.setDifficulty(item.getDifficulty());
        dto.setEstimatedDuration(item.getEstimatedDuration());
        dto.setPrerequisites(readJsonList(item.getPrerequisites()));
        dto.setResources(readJsonList(item.getResources()));
        dto.setPracticeTasks(readJsonList(item.getPracticeTasks()));
        dto.setProjectRecommendation(item.getProjectRecommendation());
        dto.setStatus(item.getStatus());
        dto.setSequenceOrder(item.getSequenceOrder());
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
