package com.career.guidance.repository;

import com.career.guidance.entity.AuditLog;
import com.career.guidance.entity.enums.AuditAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    Page<AuditLog> findAllByOrderByTimestampDesc(Pageable pageable);
    List<AuditLog> findByUserEmailOrderByTimestampDesc(String userEmail);
    List<AuditLog> findByActionOrderByTimestampDesc(AuditAction action);
}
