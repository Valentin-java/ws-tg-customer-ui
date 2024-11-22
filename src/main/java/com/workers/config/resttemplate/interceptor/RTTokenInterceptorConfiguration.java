package com.workers.config.resttemplate.interceptor;

import com.workers.config.resttemplate.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RTTokenInterceptorConfiguration implements ClientHttpRequestInterceptor {

    private final AuthService authService;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().add("Authorization", "Bearer " + authService.getToken());
        ClientHttpResponse response = execution.execute(request, body);
        return response;
    }
}
