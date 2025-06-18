package com.check.user_check.config.security.util;

import com.check.user_check.enumeratedType.TokenValid;
import com.check.user_check.exception.token.accesstoken.AccessTokenError;
import com.check.user_check.exception.token.accesstoken.AccessTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Log4j2
@Component
public class JWTUtil {

    private final SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secretKey){

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Map<String, Object> claims, int expirationTime){

        return Jwts.builder()
                .claims(claims)
                .issuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .expiration(Date.from(ZonedDateTime.now().plusMinutes(expirationTime).toInstant()))
                .signWith(secretKey)
                .compact();
    }

    public Map<String, Object> getTokenPayload(String token) throws JwtException{

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public TokenValid validateToken(String token) {

        try{
            getTokenPayload(token);
            return TokenValid.CORRECT;
        }catch (ExpiredJwtException exception){
            return TokenValid.EXPIRE;
        }catch (JwtException | IllegalArgumentException exception) {
            return TokenValid.WRONG;
        }
    }
}
