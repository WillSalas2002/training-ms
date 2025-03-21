package com.epam.training.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtService {

    public static final String DEFAULT_SUBJECT = "training-ms";
    @Value("${token.signing.key}")
    private String jwtSigningKey;

    public void validateToken(String token) {
        Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken() {
        return Jwts.builder()
                .setSubject(DEFAULT_SUBJECT)
                .setIssuedAt(new Date())
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
