package com.career.guidance.repository;

import com.career.guidance.entity.LearningRoadmap;
import com.career.guidance.entity.RoadmapItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoadmapItemRepository extends JpaRepository<RoadmapItem, UUID> {
    List<RoadmapItem> findByRoadmapOrderBySequenceOrderAsc(LearningRoadmap roadmap);
    List<RoadmapItem> findByRoadmapIdOrderBySequenceOrderAsc(UUID roadmapId);
}
