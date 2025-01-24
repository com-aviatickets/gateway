package com.aviatickets.gateway.config.filter;

import com.aviatickets.gateway.config.AppProperties.JwtProperties;
import com.aviatickets.gateway.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginFilter implements WebFilter {

    public static final String ACCESS_TOKEN_COOKIE = "aviaticketsAccessToken";

    private final JwtProperties properties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        tryToAuthenticate(exchange.getRequest());
        return chain.filter(exchange);
    }

    private void tryToAuthenticate(ServerHttpRequest request) {
        String accessToken = extractToken(request);

        if (ObjectUtils.isEmpty(accessToken)) return;

        Long id;

        try {
            id = JwtUtils.getIdFromToken(accessToken, properties.accessToken().secret());

            if (JwtUtils.isExpired(accessToken, properties.accessToken().secret())) {
                log.debug("Token for user with id {} is expired", id);
                return;
            }

        } catch (RuntimeException e) {
            log.error("Failed to extract id from token", e);
            return;
        }

        var authentication = new UsernamePasswordAuthenticationToken(
                new CustomPrincipal(id, ZonedDateTime.now()), null, Collections.emptyList()
        );

        ReactiveSecurityContextHolder.withAuthentication(authentication);
    }

    private String extractToken(ServerHttpRequest request) {
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();

        if (ObjectUtils.isEmpty(cookies)) return null;

        List<HttpCookie> accessTokenCookie = cookies.get(ACCESS_TOKEN_COOKIE);

        if (!ObjectUtils.isEmpty(accessTokenCookie)) {
            for (HttpCookie cookie : accessTokenCookie) {
                 return cookie.getValue();
            }
        }

        return null;
    }

}
