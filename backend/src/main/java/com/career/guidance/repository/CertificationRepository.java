package com.career.guidance.repository;

import com.career.guidance.entity.Certification;
import com.career.guidance.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, UUID> {
    List<Certification> findByProfile(StudentProfile profile);
    List<Certification> findByProfileId(UUID profileId);
}
