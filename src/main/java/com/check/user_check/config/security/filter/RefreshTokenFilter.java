package com.check.user_check.config.security.filter;

import com.check.user_check.exception.token.refreshToken.RefreshTokenError;
import com.check.user_check.exception.token.refreshToken.RefreshTokenException;
import com.check.user_check.config.security.util.JWTUtil;
import com.check.user_check.config.security.util.AuthenticationUtil;
import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

public class RefreshTokenFilter extends OncePerRequestFilter {

    private final String refreshPath;
    private final JWTUtil jwtUtil;

    public RefreshTokenFilter(
            String refreshPath
            , JWTUtil jwtUtil
    ){
        this.refreshPath = refreshPath;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request
            , HttpServletResponse response
            , FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        if(!path.equals(refreshPath)){
            filterChain.doFilter(request, response);
            return;
        }

        Map<String, String> tokens = AuthenticationUtil.parseRequestJSON(request);

        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");

        try {
            checkAccessToken(accessToken);
        }catch (RefreshTokenException refreshTokenException){
            refreshTokenException.sendResponseError(response);
            return;
        }

        Map<String, Object> refreshClaims = null;
        try {
            refreshClaims = checkRefreshToken(refreshToken);
        }catch (RefreshTokenException refreshTokenException){
            refreshTokenException.sendResponseError(response);
            return;
        }

        Long exp = (Long) refreshClaims.get("exp");
        Date current = new Date(System.currentTimeMillis());
        Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli() * 1000);
        long gapTime = (expTime.getTime() - current.getTime());

        int expirationMinute = AuthenticationUtil.accessTokenExpirationTime;

        String id = (String) refreshClaims.get("sub");
        String accessTokenValue = jwtUtil.generateToken(Map.of("sub", id), expirationMinute);

        String refreshTokenValue = tokens.get("refreshToken");
        if(gapTime < (1000 * 60 * expirationMinute * 3) ){
            refreshTokenValue = jwtUtil.generateToken(
                    Map.of("sub", id)
                    , expirationMinute * 4);
        }

        sendTokens(accessTokenValue, refreshTokenValue, response);
    }


    private void checkAccessToken(String accessToken) throws RefreshTokenException {

        try{
            jwtUtil.getTokenPayload(accessToken);
        }catch (ExpiredJwtException expiredJwtException){
            throw new RefreshTokenException(RefreshTokenError.EXPIRED);
        }catch (Exception exception){
            throw new RefreshTokenException(RefreshTokenError.NO_ACCESS);
        }
    }

    private Map<String, Object> checkRefreshToken(String refreshToken) throws RefreshTokenException{

        try {
            return jwtUtil.getTokenPayload(refreshToken);
        }catch (SignatureException e){
            throw new RefreshTokenException(RefreshTokenError.BAD_SIGN);
        }catch (ExpiredJwtException e) {
            throw new RefreshTokenException(RefreshTokenError.EXPIRED);
        }catch (MalformedJwtException e) {
            throw new RefreshTokenException(RefreshTokenError.MALFORMED);
        }catch (Exception e) {
            throw new RefreshTokenException(RefreshTokenError.NO_REFRESH);
        }
    }

    private void sendTokens(String accessToken, String refreshToken, HttpServletResponse httpServletResponse){
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();
        String json = gson.toJson(Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken
                )
        );

        try {
            httpServletResponse.getWriter().println(json);
        }catch (IOException ioException){
            throw new RuntimeException(ioException);
        }
    }
}
