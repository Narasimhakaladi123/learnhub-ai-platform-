package com.career.guidance.repository;

import com.career.guidance.entity.StudentProfile;
import com.career.guidance.entity.StudentProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentProjectRepository extends JpaRepository<StudentProject, UUID> {
    List<StudentProject> findByProfile(StudentProfile profile);
    List<StudentProject> findByProfileId(UUID profileId);
}
