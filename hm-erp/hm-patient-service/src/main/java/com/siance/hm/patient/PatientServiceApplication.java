package com.siance.hm.patient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {"com.siance.hm.patient", "com.siance.hm.common", "com.siance.hm.security", "com.siance.hm.kafka", "com.siance.hm.audit"})
@EntityScan(basePackages = {"com.siance.hm.patient.entity", "com.siance.hm.audit.entity"})
@EnableJpaRepositories(basePackages = {"com.siance.hm.patient.repository", "com.siance.hm.audit.repository"})
@EnableDiscoveryClient
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableCaching
@EnableAsync
public class PatientServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PatientServiceApplication.class, args);
    }
}
