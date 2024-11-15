package ru.helper.worker.config.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.helper.worker.config.resttemplate.interceptor.RTTokenInterceptorConfiguration;
import ru.helper.worker.config.resttemplate.interceptor.RestTemplateClient;

@Configuration
public class CommonConfiguration {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestTemplateClient getInterceptedRestTemplate(RTTokenInterceptorConfiguration restClientInterceptor) {
        return new RestTemplateClient(restClientInterceptor);
    }
}
