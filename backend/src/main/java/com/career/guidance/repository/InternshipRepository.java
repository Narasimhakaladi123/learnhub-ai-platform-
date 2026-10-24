package com.career.guidance.repository;

import com.career.guidance.entity.Internship;
import com.career.guidance.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InternshipRepository extends JpaRepository<Internship, UUID> {
    List<Internship> findByProfile(StudentProfile profile);
    List<Internship> findByProfileId(UUID profileId);
}
