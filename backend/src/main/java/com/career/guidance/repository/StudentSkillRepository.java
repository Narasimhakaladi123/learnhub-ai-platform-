package com.career.guidance.repository;

import com.career.guidance.entity.Skill;
import com.career.guidance.entity.StudentProfile;
import com.career.guidance.entity.StudentSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentSkillRepository extends JpaRepository<StudentSkill, UUID> {
    List<StudentSkill> findByProfile(StudentProfile profile);
    List<StudentSkill> findByProfileId(UUID profileId);
    Optional<StudentSkill> findByProfileAndSkill(StudentProfile profile, Skill skill);
    void deleteByProfileAndSkillId(StudentProfile profile, UUID skillId);
}
