package com.aviatickets.gateway.config.filter;

public record TokenDto(
        String accessToken,
        String refreshToken
) {
}
