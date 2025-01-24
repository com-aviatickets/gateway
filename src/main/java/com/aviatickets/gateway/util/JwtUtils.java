package com.aviatickets.gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Date;

public class JwtUtils {

    public static Long getIdFromToken(String token, String secret) {
        Claims claims = (Claims) Jwts.parser().setSigningKey(secret).parse(token).getBody();
        return Long.parseLong(claims.getSubject());
    }

    public static boolean isExpired(String token, String secret) {
        Date now = new Date();
        Claims claims = (Claims) Jwts.parser().setSigningKey(secret).parse(token).getBody();
        Date expiration = claims.getExpiration();
        return expiration.before(now);
    }

}
