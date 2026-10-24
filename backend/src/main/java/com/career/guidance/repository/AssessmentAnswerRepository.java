package com.career.guidance.repository;

import com.career.guidance.entity.AssessmentAnswer;
import com.career.guidance.entity.AssessmentAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssessmentAnswerRepository extends JpaRepository<AssessmentAnswer, UUID> {
    List<AssessmentAnswer> findByAttempt(AssessmentAttempt attempt);
    List<AssessmentAnswer> findByAttemptId(UUID attemptId);
}
