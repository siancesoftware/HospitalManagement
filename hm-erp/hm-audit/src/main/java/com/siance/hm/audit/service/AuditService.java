package com.siance.hm.audit.service;

import com.siance.hm.audit.entity.AuditLog.AuditAction;

import java.util.Map;

public interface AuditService {

    void logAudit(
            String entityType,
            String entityId,
            AuditAction action,
            String userId,
            String username,
            Map<String, Object> oldValue,
            Map<String, Object> newValue,
            Map<String, Object> metadata
    );

    void logCreate(
            String entityType,
            String entityId,
            String userId,
            String username,
            Map<String, Object> newValue
    );

    void logUpdate(
            String entityType,
            String entityId,
            String userId,
            String username,
            Map<String, Object> oldValue,
            Map<String, Object> newValue
    );

    void logDelete(
            String entityType,
            String entityId,
            String userId,
            String username
    );
}