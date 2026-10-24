package com.career.guidance.repository;

import com.career.guidance.entity.LearningResource;
import com.career.guidance.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LearningResourceRepository extends JpaRepository<LearningResource, UUID> {
    List<LearningResource> findBySkill(Skill skill);
    List<LearningResource> findBySkillId(UUID skillId);
}
