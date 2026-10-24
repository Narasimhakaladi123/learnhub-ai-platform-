package com.career.guidance.repository;

import com.career.guidance.entity.Education;
import com.career.guidance.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EducationRepository extends JpaRepository<Education, UUID> {
    List<Education> findByProfile(StudentProfile profile);
    List<Education> findByProfileId(UUID profileId);
}
