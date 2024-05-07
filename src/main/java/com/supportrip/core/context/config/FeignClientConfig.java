package com.supportrip.core.context.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.supportrip.core")
public class FeignClientConfig {

}
