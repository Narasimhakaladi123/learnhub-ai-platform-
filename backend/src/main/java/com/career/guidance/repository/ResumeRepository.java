package com.career.guidance.repository;

import com.career.guidance.entity.Resume;
import com.career.guidance.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, UUID> {
    List<Resume> findByProfileOrderByUploadedAtDesc(StudentProfile profile);
    List<Resume> findByProfileIdOrderByUploadedAtDesc(UUID profileId);
    Optional<Resume> findFirstByProfileOrderByUploadedAtDesc(StudentProfile profile);
}
