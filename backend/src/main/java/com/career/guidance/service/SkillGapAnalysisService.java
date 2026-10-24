package com.career.guidance.service;

import com.career.guidance.dto.response.SkillGapDto;
import com.career.guidance.entity.User;

import java.util.List;
import java.util.UUID;

public interface SkillGapAnalysisService {
    List<SkillGapDto> analyzeSkillGaps(User user, UUID careerId);
    List<SkillGapDto> getSkillGapsForTargetCareer(User user);
}
