package com.siance.hm.kafka.producer;

import com.siance.hm.common.event.BaseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public CompletableFuture<SendResult<String, Object>> publish(String topic, String key, BaseEvent event) {
        log.info("Publishing event to topic={}, key={}, type={}", topic, key, event.getEventType());
        return kafkaTemplate.send(topic, key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish event to topic={}, key={}", topic, key, ex);
                    } else {
                        log.debug("Event published successfully to topic={}, partition={}, offset={}",
                                topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                    }
                });
    }

    public CompletableFuture<SendResult<String, Object>> publish(String topic, BaseEvent event) {
        return publish(topic, event.getEventId(), event);
    }
}
