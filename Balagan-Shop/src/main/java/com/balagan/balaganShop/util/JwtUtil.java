package com.balagan.balaganShop.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtUtil {
    private final String SECRET_KEY;
    private static final long EXPIRATION_MS = 3600000; // 1 час
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        this.SECRET_KEY = secretKey;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String login, String role) {
        return Jwts.builder()
                .setSubject(login)
                .claim("role",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractLogin(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractRole(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);  // извлекаем роль из токена
    }


    public boolean validateToken(String token) {
        try {
            if (isTokenBlacklisted(token)) {
                return false;
            }
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    // Проверка, не отозван ли токен
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}