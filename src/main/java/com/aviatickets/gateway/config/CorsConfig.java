package com.aviatickets.gateway.config;

import com.aviatickets.gateway.config.AppProperties.ClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CorsConfig {

    private static final List<String> ALLOWED_METHODS = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH");

    private final ClientProperties clientProperties;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Collections.singletonList(clientProperties.host()));
        config.setAllowedMethods(ALLOWED_METHODS);
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}