package com.cafeteria.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Clave secreta fija (32 bytes mínimo)
    private final Key secretKey = Keys.hmacShaKeyFor(
            "12345678901234567890123456789012".getBytes()
    );

    private final long expiration = 86400000; // 24h

    // ============================
    // 1) GENERAR TOKEN
    // ============================
    public String generarToken(String email) {
        return Jwts.builder()
                .setSubject(email)                // <== ESTA ES LA SINTAXIS CORRECTA EN 0.11.5
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // ============================
    // 2) VALIDAR TOKEN Y OBTENER EMAIL
    // ============================
    public String obtenerEmail(String token) {
        Claims claims = Jwts.parserBuilder()       // <== ESTA ES LA SINTAXIS CORRECTA PARA 0.11.5
                .setSigningKey(secretKey)          //    NO uses "verifyWith"
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}