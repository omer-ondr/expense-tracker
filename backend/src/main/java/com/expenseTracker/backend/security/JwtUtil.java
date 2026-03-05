package com.expenseTracker.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    // Şifreleme için gizli anahtar (Canlıya alırken bunu application.properties
    // içine taşıyacağız)
    // HS256 algoritması için en az 32 karakter (256 bit) uzunluğunda olmalı!
    private static final String SECRET_KEY = "ExpenseTrackerSiberpunkGizliAnahtariBurasidir2026!";

    // Token'ın geçerlilik süresi: 1 Gün (Milisaniye cinsinden)
    private static final long EXPIRATION_TIME = 86400000;

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Kullanıcı adı (Email) ile Token Üretme
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Token içinden e-postayı (subject) çıkarma
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Token'ın süresi dolmuş mu kontrolü
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    // Token geçerli mi? (E-posta eşleşiyor mu ve süresi dolmamış mı?)
    public boolean validateToken(String token, String userEmail) {
        final String email = extractEmail(token);
        return (email.equals(userEmail) && !isTokenExpired(token));
    }
}
