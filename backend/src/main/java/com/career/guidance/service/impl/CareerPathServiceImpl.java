package com.career.guidance.service.impl;

import com.career.guidance.dto.response.CareerPathDto;
import com.career.guidance.dto.response.CareerSkillDto;
import com.career.guidance.entity.CareerPath;
import com.career.guidance.entity.CareerSkill;
import com.career.guidance.entity.enums.RequirementLevel;
import com.career.guidance.entity.enums.SkillStage;
import com.career.guidance.exception.ResourceNotFoundException;
import com.career.guidance.repository.CareerPathRepository;
import com.career.guidance.repository.CareerSkillRepository;
import com.career.guidance.service.CareerPathService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CareerPathServiceImpl implements CareerPathService {

    private final CareerPathRepository careerPathRepository;
    private final CareerSkillRepository careerSkillRepository;
    private final ObjectMapper objectMapper;

    public CareerPathServiceImpl(CareerPathRepository careerPathRepository,
                                 CareerSkillRepository careerSkillRepository) {
        this.careerPathRepository = careerPathRepository;
        this.careerSkillRepository = careerSkillRepository;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CareerPathDto> getAllCareers() {
        return careerPathRepository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CareerPathDto getCareerById(UUID id) {
        CareerPath career = careerPathRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Career path not found: " + id));
        return toDto(career);
    }

    @Override
    @Transactional(readOnly = true)
    public CareerPathDto getCareerBySlug(String slug) {
        CareerPath career = careerPathRepository.findBySlug(slug)
            .orElseThrow(() -> new ResourceNotFoundException("Career path not found with slug: " + slug));
        return toDto(career);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CareerPathDto> getCareersByCategory(String category) {
        return careerPathRepository.findByCategory(category).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public CareerPathDto toDto(CareerPath path) {
        CareerPathDto dto = new CareerPathDto();
        dto.setId(path.getId());
        dto.setName(path.getName());
        dto.setSlug(path.getSlug());
        dto.setDescription(path.getDescription());
        dto.setCategory(path.getCategory());
        dto.setAverageSalaryRange(path.getAverageSalaryRange());
        dto.setJobOutlook(path.getJobOutlook());

        // Parse responsibilities and typical roles
        dto.setResponsibilities(parseStringList(path.getResponsibilities()));
        dto.setTypicalRoles(parseStringList(path.getTypicalRoles()));

        // Fetch skills
        List<CareerSkill> careerSkills = careerSkillRepository.findByCareerPath(path);
        List<CareerSkillDto> skillDtos = careerSkills.stream()
            .map(cs -> new CareerSkillDto(
                cs.getSkill().getId(),
                cs.getSkill().getName(),
                cs.getSkill().getCategory().name(),
                cs.getRequirementLevel(),
                cs.getSkillStage(),
                cs.getMinimumProficiency(),
                cs.getWeight()
            ))
            .collect(Collectors.toList());
        dto.setSkills(skillDtos);

        // Subsets
        dto.setRequiredSkills(skillDtos.stream()
            .filter(s -> s.getRequirementLevel() == RequirementLevel.REQUIRED)
            .map(CareerSkillDto::getSkillName).collect(Collectors.toList()));
        dto.setPreferredSkills(skillDtos.stream()
            .filter(s -> s.getRequirementLevel() == RequirementLevel.PREFERRED)
            .map(CareerSkillDto::getSkillName).collect(Collectors.toList()));

        dto.setEntrySkills(skillDtos.stream()
            .filter(s -> s.getSkillStage() == SkillStage.ENTRY)
            .map(CareerSkillDto::getSkillName).collect(Collectors.toList()));
        dto.setIntermediateSkills(skillDtos.stream()
            .filter(s -> s.getSkillStage() == SkillStage.INTERMEDIATE)
            .map(CareerSkillDto::getSkillName).collect(Collectors.toList()));
        dto.setAdvancedSkills(skillDtos.stream()
            .filter(s -> s.getSkillStage() == SkillStage.ADVANCED)
            .map(CareerSkillDto::getSkillName).collect(Collectors.toList()));

        return dto;
    }

    private List<String> parseStringList(String jsonOrCsv) {
        if (jsonOrCsv == null || jsonOrCsv.isBlank()) {
            return Collections.emptyList();
        }
        try {
            if (jsonOrCsv.trim().startsWith("[")) {
                return objectMapper.readValue(jsonOrCsv, new TypeReference<List<String>>() {});
            }
        } catch (Exception ignored) {
        }
        // fallback semicolon or comma separated
        String[] parts = jsonOrCsv.split("[;,\n]");
        List<String> list = new ArrayList<>();
        for (String p : parts) {
            String trimmed = p.trim().replaceFirst("^[-*•]\\s*", "");
            if (!trimmed.isEmpty()) {
                list.add(trimmed);
            }
        }
        return list;
    }
}
