package com.volunnear.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NominatimFeignConfig {
    @Bean
    public RequestInterceptor userAgentInterceptor() {
        return template -> template.header("User-Agent", "VolunNearApp/1.0");
    }
}
