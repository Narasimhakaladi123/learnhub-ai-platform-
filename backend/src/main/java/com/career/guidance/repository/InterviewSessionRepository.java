package com.career.guidance.repository;

import com.career.guidance.entity.InterviewSession;
import com.career.guidance.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InterviewSessionRepository extends JpaRepository<InterviewSession, UUID> {
    List<InterviewSession> findByProfileOrderByCreatedAtDesc(StudentProfile profile);
    List<InterviewSession> findByProfileIdOrderByCreatedAtDesc(UUID profileId);
}
