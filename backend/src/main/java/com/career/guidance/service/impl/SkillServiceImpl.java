package com.career.guidance.service.impl;

import com.career.guidance.dto.response.SkillDto;
import com.career.guidance.entity.Skill;
import com.career.guidance.entity.enums.SkillCategory;
import com.career.guidance.exception.BadRequestException;
import com.career.guidance.exception.ResourceNotFoundException;
import com.career.guidance.repository.SkillRepository;
import com.career.guidance.service.SkillService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillDto> getAllSkills() {
        return skillRepository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillDto> getSkillsByCategory(SkillCategory category) {
        return skillRepository.findByCategory(category).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillDto> searchSkills(String query) {
        return skillRepository.findByNameContainingIgnoreCase(query).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SkillDto getSkillById(UUID id) {
        Skill skill = skillRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Skill not found: " + id));
        return toDto(skill);
    }

    @Override
    @Transactional
    public SkillDto createSkill(SkillDto dto) {
        if (skillRepository.findByNameIgnoreCase(dto.getName()).isPresent()) {
            throw new BadRequestException("Skill already exists: " + dto.getName());
        }
        Skill skill = new Skill(dto.getName(), dto.getCategory(), dto.getDescription());
        skill = skillRepository.save(skill);
        return toDto(skill);
    }

    private SkillDto toDto(Skill s) {
        return new SkillDto(s.getId(), s.getName(), s.getCategory(), s.getDescription());
    }
}
