package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpirationInMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Genera un token JWT para el usuario autenticado
     */
    public String generateToken(Long userId, String role, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("role", role)
                .claim("username", username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Genera un refresh token con expiración más larga
     */
    public String generateRefreshToken(Long userId, String role, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationInMs);

        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("role", role)
                .claim("username", username)
                .claim("type", "refresh")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Obtiene el userId desde el token
     */
    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            log.error("Error al obtener userId del token", e);
            return null;
        }
    }

    /**
     * Obtiene el role desde el token
     */
    public String getRoleFromToken(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            return claims.get("role", String.class);
        } catch (Exception e) {
            log.error("Error al obtener role del token", e);
            return null;
        }
    }

    /**
     * Obtiene el username desde el token
     */
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            return claims.get("username", String.class);
        } catch (Exception e) {
            log.error("Error al obtener username del token", e);
            return null;
        }
    }

    /**
     * Valida si el token es válido
     */
public boolean validateToken(String token) {
    try {
        Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
        return true;
    } catch (SecurityException e) {
        log.error("JWT signature validation failed: {}", e.getMessage());
    } catch (MalformedJwtException e) {
        log.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
        log.error("Expired JWT token: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
        log.error("Unsupported JWT token: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
        log.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
}

    /**
     * Obtiene todos los claims del token (uso interno)
     */
    private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}
