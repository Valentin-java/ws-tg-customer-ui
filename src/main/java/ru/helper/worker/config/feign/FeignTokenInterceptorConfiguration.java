package ru.helper.worker.config.feign;

import lombok.RequiredArgsConstructor;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import ru.helper.worker.config.resttemplate.service.AuthService;

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
