package com.siance.hm.lab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.siance.hm.lab")
public class HmLabServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(HmLabServiceApplication.class, args);
    }
}
