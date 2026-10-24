package com.career.guidance.service.impl;

import com.career.guidance.dto.response.ProjectDto;
import com.career.guidance.entity.Project;
import com.career.guidance.entity.StudentProfile;
import com.career.guidance.entity.User;
import com.career.guidance.repository.ProjectRepository;
import com.career.guidance.repository.StudentProfileRepository;
import com.career.guidance.service.ProjectRecommendationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectRecommendationServiceImpl implements ProjectRecommendationService {

    private final ProjectRepository projectRepository;
    private final StudentProfileRepository profileRepository;

    public ProjectRecommendationServiceImpl(ProjectRepository projectRepository, StudentProfileRepository profileRepository) {
        this.projectRepository = projectRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDto> getRecommendedProjects(User user) {
        StudentProfile profile = profileRepository.findByUser(user).orElse(null);
        String slug = "java-backend-developer";
        if (profile != null && profile.getTargetCareer() != null) {
            slug = profile.getTargetCareer().toLowerCase().replace(" ", "-");
        }

        List<Project> projects = projectRepository.findByTargetCareerSlug(slug);
        if (projects.isEmpty()) {
            projects = projectRepository.findAll();
        }
        return projects.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDto> getProjectsByCareerSlug(String careerSlug) {
        List<Project> projects = projectRepository.findByTargetCareerSlug(careerSlug);
        if (projects.isEmpty()) {
            projects = projectRepository.findAll();
        }
        return projects.stream().map(this::toDto).collect(Collectors.toList());
    }

    private ProjectDto toDto(Project p) {
        ProjectDto dto = new ProjectDto();
        dto.setId(p.getId());
        dto.setTitle(p.getTitle());
        dto.setDescription(p.getDescription());
        dto.setDifficulty(p.getDifficulty());
        dto.setTechnologies(parseList(p.getTechnologies()));
        dto.setSkillsDeveloped(parseList(p.getSkillsDeveloped()));
        dto.setFeatures(parseList(p.getFeatures()));
        dto.setEstimatedDurationWeeks(p.getEstimatedDurationWeeks());
        dto.setTargetCareerSlug(p.getTargetCareerSlug());
        return dto;
    }

    private List<String> parseList(String text) {
        if (text == null || text.isBlank()) return Collections.emptyList();
        return Arrays.stream(text.split("[,;\\n]"))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList());
    }
}
