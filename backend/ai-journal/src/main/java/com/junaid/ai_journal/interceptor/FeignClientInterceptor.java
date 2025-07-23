package com.junaid.ai_journal.interceptor;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Configuration
public class FeignClientInterceptor {

    /**
     * This bean creates a RequestInterceptor that will be applied to all
     * outgoing requests made by Feign clients in this service.
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                // Get the current request attributes
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

                if (attributes != null) {
                    // Get the original HTTP request
                    var httpRequest = attributes.getRequest();

                    // Extract the Authorization header (the JWT) from the original request
                    String authHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

                    // If the header exists, add it to the outgoing Feign request
                    if (Objects.nonNull(authHeader)) {
                        requestTemplate.header(HttpHeaders.AUTHORIZATION, authHeader);
                    }
                }
            }
        };
    }
}