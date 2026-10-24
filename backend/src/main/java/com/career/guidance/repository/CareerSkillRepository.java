package com.career.guidance.repository;

import com.career.guidance.entity.CareerPath;
import com.career.guidance.entity.CareerSkill;
import com.career.guidance.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CareerSkillRepository extends JpaRepository<CareerSkill, UUID> {
    List<CareerSkill> findByCareerPath(CareerPath careerPath);
    List<CareerSkill> findByCareerPathId(UUID careerId);
    Optional<CareerSkill> findByCareerPathAndSkill(CareerPath careerPath, Skill skill);
}
