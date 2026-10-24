package com.career.guidance.repository;

import com.career.guidance.entity.CareerPath;
import com.career.guidance.entity.SkillGap;
import com.career.guidance.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SkillGapRepository extends JpaRepository<SkillGap, UUID> {
    List<SkillGap> findByProfileAndCareerPathOrderBySequenceOrderAsc(StudentProfile profile, CareerPath careerPath);
    List<SkillGap> findByProfileOrderBySequenceOrderAsc(StudentProfile profile);
    List<SkillGap> findByProfileIdOrderBySequenceOrderAsc(UUID profileId);
    void deleteByProfile(StudentProfile profile);
    void deleteByProfileAndCareerPath(StudentProfile profile, CareerPath careerPath);
}
