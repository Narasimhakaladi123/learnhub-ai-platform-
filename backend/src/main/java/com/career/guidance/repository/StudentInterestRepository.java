package com.career.guidance.repository;

import com.career.guidance.entity.StudentInterest;
import com.career.guidance.entity.StudentProfile;
import com.career.guidance.entity.enums.InterestArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentInterestRepository extends JpaRepository<StudentInterest, UUID> {
    List<StudentInterest> findByProfile(StudentProfile profile);
    List<StudentInterest> findByProfileId(UUID profileId);
    void deleteByProfile(StudentProfile profile);
    boolean existsByProfileAndInterestArea(StudentProfile profile, InterestArea interestArea);
}
