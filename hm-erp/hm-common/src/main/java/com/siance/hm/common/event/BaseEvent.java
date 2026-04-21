package com.siance.hm.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.Instant;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEvent {
    private String eventId;
    private String eventType;
    private String source;
    private Instant timestamp;
    private String correlationId;
    private String userId;

    public void initDefaults(String type, String sourceService) {
        this.eventId = UUID.randomUUID().toString();
        this.eventType = type;
        this.source = sourceService;
        this.timestamp = Instant.now();
    }
}
