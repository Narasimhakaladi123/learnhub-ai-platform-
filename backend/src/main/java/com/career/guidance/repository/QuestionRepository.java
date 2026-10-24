package com.career.guidance.repository;

import com.career.guidance.entity.Assessment;
import com.career.guidance.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {
    List<Question> findByAssessment(Assessment assessment);
    List<Question> findByAssessmentId(UUID assessmentId);
}
