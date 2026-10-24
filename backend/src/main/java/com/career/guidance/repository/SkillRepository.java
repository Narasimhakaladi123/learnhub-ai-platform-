package com.career.guidance.repository;

import com.career.guidance.entity.Skill;
import com.career.guidance.entity.enums.SkillCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SkillRepository extends JpaRepository<Skill, UUID> {
    Optional<Skill> findByNameIgnoreCase(String name);
    List<Skill> findByCategory(SkillCategory category);
    List<Skill> findByNameContainingIgnoreCase(String keyword);
}
