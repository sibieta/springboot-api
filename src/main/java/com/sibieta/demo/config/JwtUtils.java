package com.sibieta.demo.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import com.sibieta.demo.model.Usuario;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secretKey;
    private final long expirationTimeMillis = 86400000;

    public String generateToken(Usuario user) {
        return Jwts.builder()
                .setSubject(user.getEmail().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis))
                // Add other claims if needed (e.g., roles, username)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {            
            return false;
        }
    }
}
