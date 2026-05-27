package org.god.godecommerce.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.god.godecommerce.security.jwt.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private final SecretKey key;
    private final String jwtCookieName;
    private final Duration tokenValidity;

    public JwtUtils(@Value("${jwt.secret}") String secret,
                    @Value("${jwt.cookie.name}") String jwtCookieName,
                    @Value("${jwt.token.expiration}") Duration tokenValidity) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.jwtCookieName = jwtCookieName;
        this.tokenValidity = tokenValidity;
    }

    public String getJwtFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookieName);
        return cookie != null ? cookie.getValue() : null;
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userDetails) {
        String token = generateTokenFromUserName(userDetails.userName());
        return ResponseCookie.from(jwtCookieName, token)
                .httpOnly(true)
                .sameSite("Strict")
                .maxAge(Duration.ofMinutes(8))
                .path("/api")
                .build();
    }

    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookieName, null)
                .httpOnly(true)
                .maxAge(0)
                .path("/api")
                .build();
    }


    public String generateTokenFromUserName(String userName) {
        Instant now = Instant.now();
        Instant expiry = now.plus(tokenValidity);
        return Jwts.builder()
                .subject(userName)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(key)
                .compact();
    }

    public String getUserNameFromJWTToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token is expired: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.error("JWT Signature does not match: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT token is null, empty or whitespace: {}", e.getMessage());
        }
        return false;
    }
}
