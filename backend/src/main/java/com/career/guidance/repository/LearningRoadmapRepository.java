package com.career.guidance.repository;

import com.career.guidance.entity.CareerPath;
import com.career.guidance.entity.LearningRoadmap;
import com.career.guidance.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LearningRoadmapRepository extends JpaRepository<LearningRoadmap, UUID> {
    List<LearningRoadmap> findByProfile(StudentProfile profile);
    List<LearningRoadmap> findByProfileId(UUID profileId);
    Optional<LearningRoadmap> findByProfileAndCareerPath(StudentProfile profile, CareerPath careerPath);
    Optional<LearningRoadmap> findFirstByProfileOrderByUpdatedAtDesc(StudentProfile profile);
}
