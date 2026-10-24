package com.career.guidance.service;

import com.career.guidance.dto.response.SkillDto;
import com.career.guidance.entity.Skill;
import com.career.guidance.entity.enums.SkillCategory;

import java.util.List;
import java.util.UUID;

public interface SkillService {
    List<SkillDto> getAllSkills();
    List<SkillDto> getSkillsByCategory(SkillCategory category);
    List<SkillDto> searchSkills(String query);
    SkillDto getSkillById(UUID id);
    SkillDto createSkill(SkillDto dto);
}
