package com.helpdesk.infrastructure.security;

import com.helpdesk.application.port.JwtService;
import com.helpdesk.domain.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * Genera, valida y lee tokens JWT firmados con HMAC.
 */
@Component
public class JwtTokenProvider implements JwtService {

    private final SecretKey secretKey;
    private final long expirationMs;
    private final long refreshExpirationMs;

    public JwtTokenProvider(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration}") long expirationMs,
            @Value("${app.jwt.refresh-expiration}") long refreshExpirationMs
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    @Override
    public TokenPar generarTokens(Usuario usuario) {
        Map<String, Object> claims = Map.of(
                "uid", usuario.getId(),
                "rol", usuario.getRol().name(),
                "typ", "access"
        );
        String accessToken = generarToken(usuario.getCorreoElectronico(), claims, expirationMs);
        String refreshToken = generarToken(usuario.getCorreoElectronico(), Map.of("typ", "refresh"), refreshExpirationMs);
        return new TokenPar(accessToken, refreshToken, expirationMs);
    }

    @Override
    public TokenPar renovar(String refreshToken) {
        Claims claims = obtenerClaims(refreshToken);
        if (!"refresh".equals(claims.get("typ", String.class))) {
            throw new IllegalArgumentException("Token de refresh invalido");
        }
        String username = claims.getSubject();
        String accessToken = generarToken(username, Map.of("typ", "access"), expirationMs);
        String nuevoRefreshToken = generarToken(username, Map.of("typ", "refresh"), refreshExpirationMs);
        return new TokenPar(accessToken, nuevoRefreshToken, expirationMs);
    }

    public String generarToken(String username, Map<String, Object> claims) {
        return generarToken(username, claims, expirationMs);
    }

    public boolean validarToken(String token) {
        try {
            obtenerClaims(token);
            return true;
        } catch (RuntimeException ex) {
            return false;
        }
    }

    public String obtenerUsername(String token) {
        return obtenerClaims(token).getSubject();
    }

    public Date obtenerExpiracion(String token) {
        return obtenerClaims(token).getExpiration();
    }

    private String generarToken(String username, Map<String, Object> claims, long duracionMs) {
        Instant ahora = Instant.now();
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plusMillis(duracionMs)))
                .signWith(secretKey)
                .compact();
    }

    private Claims obtenerClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
