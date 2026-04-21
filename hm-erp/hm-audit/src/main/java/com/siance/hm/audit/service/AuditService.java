package com.siance.hm.audit.service;

import com.siance.hm.audit.entity.AuditLog;
import com.siance.hm.audit.entity.AuditLog.AuditAction;
import com.siance.hm.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    @Value("${spring.application.name:unknown}")
    private String serviceName;

    @Async
    public void logAudit(String entityType, String entityId, AuditAction action,
                         String userId, String username, Map<String, Object> oldValue,
                         Map<String, Object> newValue, Map<String, Object> metadata) {
        try {
            AuditLog auditLog = AuditLog.builder()
                    .entityType(entityType)
                    .entityId(entityId)
                    .action(action)
                    .userId(userId)
                    .username(username)
                    .serviceName(serviceName)
                    .oldValue(oldValue)
                    .newValue(newValue)
                    .metadata(metadata)
                    .timestamp(Instant.now())
                    .build();
            auditLogRepository.save(auditLog);
            log.debug("Audit log saved: {} {} {}", action, entityType, entityId);
        } catch (Exception e) {
            log.error("Failed to save audit log for {} {} {}", action, entityType, entityId, e);
        }
    }

    public void logCreate(String entityType, String entityId, String userId, String username, Map<String, Object> newValue) {
        logAudit(entityType, entityId, AuditAction.CREATE, userId, username, null, newValue, null);
    }

    public void logUpdate(String entityType, String entityId, String userId, String username, Map<String, Object> oldValue, Map<String, Object> newValue) {
        logAudit(entityType, entityId, AuditAction.UPDATE, userId, username, oldValue, newValue, null);
    }

    public void logDelete(String entityType, String entityId, String userId, String username) {
        logAudit(entityType, entityId, AuditAction.DELETE, userId, username, null, null, null);
    }
}
