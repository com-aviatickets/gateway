package com.aviatickets.gateway.config.filter;

import java.time.ZonedDateTime;

public record CustomPrincipal(
        Long userId,
        ZonedDateTime lastLogin
) {
}
