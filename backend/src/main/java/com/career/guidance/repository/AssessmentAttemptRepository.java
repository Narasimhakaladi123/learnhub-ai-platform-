package com.career.guidance.repository;

import com.career.guidance.entity.Assessment;
import com.career.guidance.entity.AssessmentAttempt;
import com.career.guidance.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssessmentAttemptRepository extends JpaRepository<AssessmentAttempt, UUID> {
    List<AssessmentAttempt> findByProfileOrderByAttemptedAtDesc(StudentProfile profile);
    List<AssessmentAttempt> findByProfileIdOrderByAttemptedAtDesc(UUID profileId);
    List<AssessmentAttempt> findByAssessmentOrderByAttemptedAtDesc(Assessment assessment);
    Optional<AssessmentAttempt> findFirstByProfileAndAssessmentOrderByScoreDesc(StudentProfile profile, Assessment assessment);
}
