package com.bamboo.baekjoon.global.config.security;

import com.bamboo.baekjoon.domain.user.User;
import com.bamboo.baekjoon.domain.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(TokenService.class);
    private static final String AUTHORITIES_KEY = "auth";

    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    @Value("${jwt.token-validity-in-seconds}")
    private int VALIDITY;

    private Key key;

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String uid, String role) {
        Claims claims = Jwts.claims().setSubject(uid);
        claims.put("role", role);

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + VALIDITY * 1000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUidFromToken(String token) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();

        return parser.parseClaimsJws(token).getBody().getSubject();
    }

    public User getUserByToken(String token) {
        return userRepository.findById(Long.valueOf(getUidFromToken(token))).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다.");
        });
    }

    public String getUserIdFromJwt(String token) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();
        Claims claims = parser.parseClaimsJwt(token).getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
