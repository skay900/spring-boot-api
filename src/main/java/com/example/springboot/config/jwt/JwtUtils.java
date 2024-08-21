package com.example.springboot.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${jwt.secretKey}")
    private String secretKey;

    private Key key;

    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 3600000; // 1 hour in milliseconds
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateAccessToken(String username) {
        return createToken(username, ACCESS_TOKEN_EXPIRATION_TIME);
    }

    public String generateRefreshToken(String username) {
        return createToken(username, REFRESH_TOKEN_EXPIRATION_TIME);
    }

    private String createToken(String subject, long expirationTime) {
        return Jwts.builder()
                .setClaims(setClaims(subject, expirationTime))
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Map<String, Object> setClaims(String username, long expirationTime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);
        claims.put("iss", "spring-boot-api");
        claims.put("iat", new Date(System.currentTimeMillis()));
        claims.put("exp", new Date(System.currentTimeMillis() + expirationTime));
        claims.put("username", username);
        claims.put("roles", "USER");
        return claims;
    }

    public Map<String, Object> decodeAccessToken(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        Claims claims = claimsJws.getBody();

        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("subject", claims.getSubject());
        claimsMap.put("issuer", claims.getIssuer());
        claimsMap.put("issuedAt", claims.getIssuedAt());
        claimsMap.put("expiration", claims.getExpiration());
        claimsMap.put("username", claims.get("username"));
        claimsMap.put("roles", claims.get("roles"));

        return claimsMap;
    }

}