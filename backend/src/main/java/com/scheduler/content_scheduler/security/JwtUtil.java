package com.scheduler.content_scheduler.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private static final long JWT_EXPIRATION_MS = 1000L * 60 * 60 * 10;

    private final Key signingKey;

    public JwtUtil(@Value("${jwt.secret.key}") String secret) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException(
                    "JWT secret key must be at least 32 characters (256 bits for HS256)"
            );
        }
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String username) {
        try {
            Claims claims = extractClaims(token);
            return claims.getSubject().equals(username)
                    && claims.getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
