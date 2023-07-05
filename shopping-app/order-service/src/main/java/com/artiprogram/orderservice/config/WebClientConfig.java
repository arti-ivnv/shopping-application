package com.artiprogram.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

// Achieving inter process communication (HttpRequest) via WebClient class
@Configuration
public class WebClientConfig {
    
    
    // WebClient class is a part of spring Web Flux dependency
    @Bean
    public WebClient webClient(){
        return WebClient.builder().build();
    }
}
