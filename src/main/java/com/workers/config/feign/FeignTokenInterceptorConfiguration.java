package com.workers.config.feign;

import com.workers.config.resttemplate.service.AuthService;
import lombok.RequiredArgsConstructor;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FeignTokenInterceptorConfiguration {

    private final AuthService authService;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Accept", ContentType.APPLICATION_JSON.getMimeType());
            requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + authService.getToken());
        };
    }
}
