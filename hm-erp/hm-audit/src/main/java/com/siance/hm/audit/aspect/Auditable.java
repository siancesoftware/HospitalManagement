package com.siance.hm.audit.aspect;

import com.siance.hm.audit.entity.AuditLog.AuditAction;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auditable {
    AuditAction action();
    String entityType();
}
