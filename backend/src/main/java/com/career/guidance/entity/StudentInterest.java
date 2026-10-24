package com.career.guidance.entity;

import com.career.guidance.entity.enums.InterestArea;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "student_interests", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"profile_id", "interest_area"})
})
public class StudentInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private StudentProfile profile;

    @Enumerated(EnumType.STRING)
    @Column(name = "interest_area", nullable = false, length = 50)
    private InterestArea interestArea;

    public StudentInterest() {
    }

    public StudentInterest(StudentProfile profile, InterestArea interestArea) {
        this.profile = profile;
        this.interestArea = interestArea;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public StudentProfile getProfile() {
        return profile;
    }

    public void setProfile(StudentProfile profile) {
        this.profile = profile;
    }

    public InterestArea getInterestArea() {
        return interestArea;
    }

    public void setInterestArea(InterestArea interestArea) {
        this.interestArea = interestArea;
    }
}
