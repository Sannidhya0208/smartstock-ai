package com.smartstock.backend.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey signingKey;

    public JwtService(
            @Value("${jwt.secret}") String secretKey
    ) {
        if (
                secretKey == null
                        || secretKey.isBlank()
                        || secretKey.length() < 32
        ) {
            throw new IllegalStateException(
                    "JWT_SECRET must contain at least 32 characters"
            );
        }

        this.signingKey = Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateToken(String email) {
        Date issuedAt = new Date();

        Date expiration = new Date(
                issuedAt.getTime()
                        + 1000L * 60 * 60 * 24
        );

        return Jwts.builder()
                .subject(email)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(signingKey)
                .compact();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token)
                .getSubject();
    }

    public boolean isTokenValid(
            String token,
            UserDetails userDetails
    ) {
        String email = extractEmail(token);

        return email != null
                && email.equals(
                        userDetails.getUsername()
                )
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token)
                .before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token)
                .getExpiration();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}