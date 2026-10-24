package com.career.guidance.repository;

import com.career.guidance.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {
    Optional<Assessment> findBySlug(String slug);
    List<Assessment> findByCategory(String category);
}
