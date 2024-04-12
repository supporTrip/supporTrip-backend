package com.supportrip.core.config;

import com.supportrip.core.auth.kakao.KakaoAuthAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebConfig {

    @Bean
    public KakaoAuthAPI KakaoAuthAPI() {
        RestClient restClient = RestClient.create();
        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();

        return factory.createClient(KakaoAuthAPI.class);
    }
}
