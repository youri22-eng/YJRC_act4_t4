package com.youri.act4t4.seguridad;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServicio {

    @Value("${app.jwt.secret}")
    private String secreto;

    @Value("${app.jwt.expiracion-ms}")
    private long expiracionMs;

    private SecretKey clave() {
        return Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
    }

    // Crea un token firmado con el email como "subject" y una fecha de vencimiento
    public String generarToken(String email) {
        Date ahora = new Date();
        return Jwts.builder()
                .subject(email)
                .issuedAt(ahora)
                .expiration(new Date(ahora.getTime() + expiracionMs))
                .signWith(clave())
                .compact();
    }

    // Valida la firma y el vencimiento; si el token es invalido lanza una excepcion
    public String extraerEmail(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(clave())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }
}