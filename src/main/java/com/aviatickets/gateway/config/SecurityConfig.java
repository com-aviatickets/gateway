package com.aviatickets.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(
                                "/profile/api/**",
                                "/booking/api/**,",
                                "/flight/api/**",
                                "/notification/api/**",
                                "/payment/api/**"
                                ).authenticated()
                        .anyExchange().permitAll()
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }

}