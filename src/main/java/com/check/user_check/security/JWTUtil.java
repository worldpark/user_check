package com.check.user_check.security;

import com.check.user_check.dto.UserDto;
import com.check.user_check.security.dto.JwtInfoDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Log4j2
public class JWTUtil {

    private final Key secretKey;
    private final Long accessTokenExpireTime;
    private final Long refreshTokenExpireTime;

    public JWTUtil(@Value("$spring.jwt.secretKey") String secretKey,
                   @Value("${jwt.access_expiration_time}") Long accessTokenExpireTime,
                   @Value("${jwt.refresh_expiration_time}") Long refreshTokenExpireTime){

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    public JwtInfoDto createToken(UserDto userDto){
        Date accessTokenExpireTime = new Date(System.currentTimeMillis() + this.accessTokenExpireTime);
        Date refreshTokenExpireTime = new Date(System.currentTimeMillis() + this.refreshTokenExpireTime);

        String accessToken = createAccessToken(userDto, accessTokenExpireTime);
        String refreshToken = createRefreshToken(userDto, refreshTokenExpireTime);

        return JwtInfoDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpireTime)
                .refreshToken(refreshToken)
                .refreshTokenExpireTime(refreshTokenExpireTime)
                .build();
    }

    private String createAccessToken(UserDto userDto, Date expirationTime){

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject("AccessToken")
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
                .claim("userId", userDto.userId())
                .claim("userName", userDto.userName())
                .claim("role", userDto.role())
                .signWith(this.secretKey, SignatureAlgorithm.ES256)
                .compact();
    }

    private String createRefreshToken(UserDto userDto, Date expirationTime){

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject("RefreshToken")
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
                .claim("userId", userDto.userId())
                .claim("userName", userDto.userName())
                .signWith(this.secretKey, SignatureAlgorithm.ES256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(this.secretKey).build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException exception) {
            throw new MalformedJwtException("JWT 토큰이 유효하지 않습니다.");

        } catch (ExpiredJwtException expiredJwtException) {

        } catch (UnsupportedJwtException unsupportedJwtException) {
            throw new UnsupportedJwtException("지원 하지 않는 JWT 토큰 입니다.");

        } catch (IllegalArgumentException illegalArgumentException) {
            throw new IllegalArgumentException("JWT claims가 비어 있습니다.");
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(this.secretKey).build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String getUserId(String token){
        return parseClaims(token).get("userId", String.class);
    }
}
