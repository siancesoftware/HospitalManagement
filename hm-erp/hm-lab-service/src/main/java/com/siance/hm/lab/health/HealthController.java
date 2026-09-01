package com.siance.hm.lab.health;

import com.siance.hm.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.Map;

@Tag(name = "Health")
@RestController
@RequestMapping("/health")
public class HealthController {
    @GetMapping
    public ApiResponse<Map<String, Object>> health() {
        return ApiResponse.of(Map.of("status", "ok", "service", "hm-lab-service",
                "timestamp", OffsetDateTime.now().toString()));
    }

    @GetMapping("/ping")
    public ApiResponse<String> ping() {
        return ApiResponse.of("pong");
    }
}
