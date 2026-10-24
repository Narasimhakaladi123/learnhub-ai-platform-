package com.career.guidance.repository;

import com.career.guidance.entity.Experience;
import com.career.guidance.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, UUID> {
    List<Experience> findByProfile(StudentProfile profile);
    List<Experience> findByProfileId(UUID profileId);
}
