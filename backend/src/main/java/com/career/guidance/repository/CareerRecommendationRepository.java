package com.career.guidance.repository;

import com.career.guidance.entity.CareerPath;
import com.career.guidance.entity.CareerRecommendation;
import com.career.guidance.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CareerRecommendationRepository extends JpaRepository<CareerRecommendation, UUID> {
    List<CareerRecommendation> findByProfileOrderByCompatibilityScoreDesc(StudentProfile profile);
    List<CareerRecommendation> findByProfileIdOrderByCompatibilityScoreDesc(UUID profileId);
    Optional<CareerRecommendation> findByProfileAndCareerPath(StudentProfile profile, CareerPath careerPath);
    void deleteByProfile(StudentProfile profile);
}
