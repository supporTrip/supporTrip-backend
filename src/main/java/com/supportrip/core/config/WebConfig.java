package com.supportrip.core.config;

import com.supportrip.core.auth.kakao.KakaoAuthAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private static final String ALLOWED_METHODS = "GET,HEAD,POST,DELETE,TRACE,OPTIONS,PATCH,PUT";

    @Bean
    public KakaoAuthAPI KakaoAuthAPI() {
        RestClient restClient = RestClient.create();
        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();

        return factory.createClient(KakaoAuthAPI.class);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://www.supportrip.world", "http://localhost:5173")
                .allowedMethods(ALLOWED_METHODS.split(","))
                .allowedHeaders("*")
                .allowCredentials(true)
                .exposedHeaders(HttpHeaders.LOCATION);
    }
}
