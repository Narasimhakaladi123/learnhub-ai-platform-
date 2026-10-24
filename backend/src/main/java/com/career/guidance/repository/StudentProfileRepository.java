package com.career.guidance.repository;

import com.career.guidance.entity.StudentProfile;
import com.career.guidance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, UUID> {
    Optional<StudentProfile> findByUser(User user);
    Optional<StudentProfile> findByUserEmail(String email);
    Optional<StudentProfile> findByUserId(UUID userId);
}
