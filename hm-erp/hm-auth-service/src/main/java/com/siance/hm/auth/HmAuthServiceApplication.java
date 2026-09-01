package com.siance.hm.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.siance.hm.auth")
public class HmAuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(HmAuthServiceApplication.class, args);
    }
}
