package com.career.guidance.repository;

import com.career.guidance.entity.AIConversation;
import com.career.guidance.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AIConversationRepository extends JpaRepository<AIConversation, UUID> {
    List<AIConversation> findByProfileOrderByUpdatedAtDesc(StudentProfile profile);
    List<AIConversation> findByProfileIdOrderByUpdatedAtDesc(UUID profileId);
    Optional<AIConversation> findFirstByProfileOrderByUpdatedAtDesc(StudentProfile profile);
}
