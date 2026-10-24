package com.career.guidance.service;

import com.career.guidance.entity.AuditLog;
import com.career.guidance.entity.enums.AuditAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditLogService {
    void log(String userEmail, AuditAction action, String details, String ipAddress);
    Page<AuditLog> getLogs(Pageable pageable);
}
