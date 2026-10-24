package com.career.guidance.service.impl;

import com.career.guidance.entity.AuditLog;
import com.career.guidance.entity.enums.AuditAction;
import com.career.guidance.repository.AuditLogRepository;
import com.career.guidance.service.AuditLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private static final Logger log = LoggerFactory.getLogger(AuditLogServiceImpl.class);
    private final AuditLogRepository auditLogRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public void log(String userEmail, AuditAction action, String details, String ipAddress) {
        try {
            AuditLog auditLog = new AuditLog(userEmail, action, details, ipAddress);
            auditLogRepository.save(auditLog);
            log.info("AUDIT: [{}] by [{}] - {}", action, userEmail, details);
        } catch (Exception ex) {
            log.error("Failed to record audit log: {}", ex.getMessage());
        }
    }

    @Override
    public Page<AuditLog> getLogs(Pageable pageable) {
        return auditLogRepository.findAllByOrderByTimestampDesc(pageable);
    }
}
