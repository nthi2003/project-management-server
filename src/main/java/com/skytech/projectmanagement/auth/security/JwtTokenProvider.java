package com.skytech.projectmanagement.auth.security;

import java.security.Key;
import java.util.Date;
import com.skytech.projectmanagement.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {

    private final String jwtSecret;
    private final long jwtExpirationMs;
    private final long jwtRefreshExpirationMs;

    public JwtTokenProvider(@Value("${jwt.secret-key}") String jwtSecret,
            @Value("${jwt.expiration-ms}") long jwtExpirationMs,
            @Value("${jwt.refresh-expiration-ms}") long jwtRefreshExpirationMs) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationMs = jwtExpirationMs;
        this.jwtRefreshExpirationMs = jwtRefreshExpirationMs;
    }

    private String createToken(String email, long expirationMs) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + expirationMs);

        return Jwts.builder().setSubject(email).setIssuedAt(currentDate).setExpiration(expireDate)
                .signWith(key()).compact();
    }

    public String generateJwtToken(Authentication authentication) {
        String email = authentication.getName();
        return createToken(email, jwtExpirationMs);
    }

    public String generateJwtToken(User user) {
        return createToken(user.getEmail(), jwtExpirationMs);
    }

    public String generateRefreshToken(Authentication authentication) {
        String email = authentication.getName();
        return createToken(email, jwtRefreshExpirationMs);
    }

    public String generateRefreshToken(User user) {
        return createToken(user.getEmail(), jwtRefreshExpirationMs);
    }

    public String getEmail(String token) {
        Claims claims =
                Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();
        String email = claims.getSubject();
        return email;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("JWT token không hợp lệ: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token hết hạn: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token không được hỗ trợ: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims rỗng: {}", e.getMessage());
        }
        return false;
    }

    public Date getExpirationDateFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody()
                .getExpiration();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
