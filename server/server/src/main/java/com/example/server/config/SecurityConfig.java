package com.example.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // 明确使用 BCrypt 版本 2a
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2A, 10);
    }
}