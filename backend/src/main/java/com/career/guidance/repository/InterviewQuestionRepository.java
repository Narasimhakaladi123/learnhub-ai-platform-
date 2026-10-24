package com.career.guidance.repository;

import com.career.guidance.entity.InterviewQuestion;
import com.career.guidance.entity.InterviewSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, UUID> {
    List<InterviewQuestion> findBySession(InterviewSession session);
    List<InterviewQuestion> findBySessionId(UUID sessionId);
}
