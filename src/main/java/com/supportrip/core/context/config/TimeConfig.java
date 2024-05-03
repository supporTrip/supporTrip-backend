package com.supportrip.core.context.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@Configuration
@EnableScheduling
public class TimeConfig {
    public static final String ZONE = "Asia/Seoul";

    @PostConstruct
    void initTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZONE));
    }
}