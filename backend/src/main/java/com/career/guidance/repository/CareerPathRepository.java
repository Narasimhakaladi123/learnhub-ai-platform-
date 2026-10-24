package com.career.guidance.repository;

import com.career.guidance.entity.CareerPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CareerPathRepository extends JpaRepository<CareerPath, UUID> {
    Optional<CareerPath> findBySlug(String slug);
    Optional<CareerPath> findByNameIgnoreCase(String name);
    List<CareerPath> findByCategory(String category);
}
