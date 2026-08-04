package com.smartstock.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class HostedLlmConfig {

    @Bean
    public RestClient hostedLlmRestClient(
            @Value("${llm.base-url}") String baseUrl,
            @Value("${llm.api-key}") String apiKey
    ) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "Groq API key is not configured"
            );
        }

        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + apiKey
                )
                .defaultHeader(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE
                )
                .defaultHeader(
                        HttpHeaders.ACCEPT,
                        MediaType.APPLICATION_JSON_VALUE
                )
                .build();
 
                
            }
}