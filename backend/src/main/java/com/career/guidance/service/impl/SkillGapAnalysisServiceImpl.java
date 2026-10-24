package com.career.guidance.service.impl;

import com.career.guidance.dto.response.SkillGapDto;
import com.career.guidance.entity.*;
import com.career.guidance.entity.enums.PriorityLevel;
import com.career.guidance.entity.enums.ProficiencyLevel;
import com.career.guidance.entity.enums.RequirementLevel;
import com.career.guidance.exception.ResourceNotFoundException;
import com.career.guidance.repository.*;
import com.career.guidance.service.SkillGapAnalysisService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SkillGapAnalysisServiceImpl implements SkillGapAnalysisService {

    private final StudentProfileRepository profileRepository;
    private final CareerPathRepository careerPathRepository;
    private final CareerSkillRepository careerSkillRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final SkillGapRepository skillGapRepository;

    public SkillGapAnalysisServiceImpl(
            StudentProfileRepository profileRepository,
            CareerPathRepository careerPathRepository,
            CareerSkillRepository careerSkillRepository,
            StudentSkillRepository studentSkillRepository,
            SkillGapRepository skillGapRepository) {
        this.profileRepository = profileRepository;
        this.careerPathRepository = careerPathRepository;
        this.careerSkillRepository = careerSkillRepository;
        this.studentSkillRepository = studentSkillRepository;
        this.skillGapRepository = skillGapRepository;
    }

    @Override
    @Transactional
    public List<SkillGapDto> analyzeSkillGaps(User user, UUID careerId) {
        StudentProfile profile = profileRepository.findByUser(user)
            .orElseGet(() -> profileRepository.save(new StudentProfile(user)));

        CareerPath career = careerPathRepository.findById(careerId)
            .orElseThrow(() -> new ResourceNotFoundException("Career path not found: " + careerId));

        List<CareerSkill> careerSkills = careerSkillRepository.findByCareerPath(career);
        List<StudentSkill> studentSkills = studentSkillRepository.findByProfile(profile);

        Map<UUID, StudentSkill> studentSkillMap = studentSkills.stream()
            .collect(Collectors.toMap(ss -> ss.getSkill().getId(), ss -> ss, (a, b) -> a));

        // Delete old gaps for this career
        skillGapRepository.deleteByProfileAndCareerPath(profile, career);

        List<SkillGap> gaps = new ArrayList<>();
        int sequence = 1;

        // Group into Required vs Preferred
        for (CareerSkill cs : careerSkills) {
            Skill skill = cs.getSkill();
            ProficiencyLevel requiredProf = cs.getMinimumProficiency();
            StudentSkill ss = studentSkillMap.get(skill.getId());

            if (ss == null) {
                // Missing entirely
                PriorityLevel priority = cs.getRequirementLevel() == RequirementLevel.REQUIRED
                    ? PriorityLevel.PRIORITY_1
                    : PriorityLevel.PRIORITY_2;

                gaps.add(new SkillGap(
                    profile, career, skill, skill.getName(),
                    null, requiredProf, priority, sequence++
                ));
            } else if (ss.getProficiency().getLevelWeight() < requiredProf.getLevelWeight()) {
                // Weak skill
                PriorityLevel priority = cs.getRequirementLevel() == RequirementLevel.REQUIRED
                    ? PriorityLevel.PRIORITY_1
                    : PriorityLevel.PRIORITY_3;

                gaps.add(new SkillGap(
                    profile, career, skill, skill.getName(),
                    ss.getProficiency(), requiredProf, priority, sequence++
                ));
            }
        }

        // Sort gaps by priority level (PRIORITY_1, then 2, then 3)
        gaps.sort(Comparator.comparing(SkillGap::getPriorityLevel));
        for (int i = 0; i < gaps.size(); i++) {
            gaps.get(i).setSequenceOrder(i + 1);
        }

        List<SkillGap> savedGaps = skillGapRepository.saveAll(gaps);

        return savedGaps.stream()
            .map(g -> new SkillGapDto(
                g.getSkill().getId(),
                g.getSkillName(),
                g.getCurrentProficiency(),
                g.getRequiredProficiency(),
                g.getPriorityLevel(),
                g.getSequenceOrder()
            ))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<SkillGapDto> getSkillGapsForTargetCareer(User user) {
        StudentProfile profile = profileRepository.findByUser(user)
            .orElseGet(() -> profileRepository.save(new StudentProfile(user)));

        String target = profile.getTargetCareer();
        CareerPath career;
        if (target != null && !target.isBlank()) {
            career = careerPathRepository.findByNameIgnoreCase(target)
                .orElseGet(() -> careerPathRepository.findAll().stream().findFirst().orElse(null));
        } else {
            career = careerPathRepository.findAll().stream().findFirst().orElse(null);
        }

        if (career == null) {
            return Collections.emptyList();
        }

        List<SkillGap> existing = skillGapRepository.findByProfileAndCareerPathOrderBySequenceOrderAsc(profile, career);
        if (existing.isEmpty()) {
            return analyzeSkillGaps(user, career.getId());
        }

        return existing.stream()
            .map(g -> new SkillGapDto(
                g.getSkill().getId(),
                g.getSkillName(),
                g.getCurrentProficiency(),
                g.getRequiredProficiency(),
                g.getPriorityLevel(),
                g.getSequenceOrder()
            ))
            .collect(Collectors.toList());
    }
}
