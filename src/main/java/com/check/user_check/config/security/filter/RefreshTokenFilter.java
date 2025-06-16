package com.check.user_check.config.security.filter;

import com.check.user_check.entity.RefreshTokenRecord;
import com.check.user_check.exception.token.refreshToken.RefreshTokenError;
import com.check.user_check.exception.token.refreshToken.RefreshTokenException;
import com.check.user_check.config.security.util.JWTUtil;
import com.check.user_check.service.response.basic.RefreshTokenService;
import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

public class RefreshTokenFilter extends OncePerRequestFilter {

    private final String refreshPath;
    private final JWTUtil jwtUtil;

    private final int accessTokenExpirationTime;
    public final int refreshTokenExpirationTime;

    public final RefreshTokenService refreshTokenService;

    public RefreshTokenFilter(
            String refreshPath
            , JWTUtil jwtUtil
            , int refreshTokenExpirationTime
            , int accessTokenExpirationTime
            , RefreshTokenService refreshTokenService
    ){
        this.refreshPath = refreshPath;
        this.jwtUtil = jwtUtil;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenService = refreshTokenService;
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

        String refreshToken = getRefreshToken(request);
        refreshTokenService.findByTokenValue(refreshToken);

        Map<String, Object> refreshClaims = null;
        try {
            refreshClaims = checkRefreshToken(refreshToken);
        }catch (RefreshTokenException refreshTokenException){
            refreshTokenException.sendResponseError(response);
            return;
        }

        Long exp = (Long) refreshClaims.get("exp");
        Date current = new Date(System.currentTimeMillis());
        Date expTime = new Date(exp);
        long gapTime = (expTime.getTime() - current.getTime());

        String username = (String) refreshClaims.get("sub");
        String accessTokenValue = jwtUtil.generateToken(Map.of("sub", username), accessTokenExpirationTime);

        if(gapTime < (refreshTokenExpirationTime / 3) ){

            refreshTokenService.deleteByTokenValue(refreshToken);
            refreshToken = jwtUtil.generateToken(Map.of("sub", username), refreshTokenExpirationTime);
        }

        sendTokens(accessTokenValue, refreshToken, response, username);
    }

    private Map<String, Object> checkRefreshToken(String refreshToken) throws RefreshTokenException{

        try {
            return jwtUtil.getTokenPayload(refreshToken);
        }catch (SignatureException e){
            throw new RefreshTokenException(RefreshTokenError.BAD_SIGN);
        }catch (ExpiredJwtException e) {
            refreshTokenService.deleteByTokenValue(refreshToken);

            throw new RefreshTokenException(RefreshTokenError.EXPIRED);
        }catch (MalformedJwtException e) {
            throw new RefreshTokenException(RefreshTokenError.MALFORMED);
        }catch (Exception e) {
            throw new RefreshTokenException(RefreshTokenError.NO_REFRESH);
        }
    }

    private void sendTokens(String accessToken,
                            String refreshToken,
                            HttpServletResponse httpServletResponse,
                            String username){
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();
        String json = gson.toJson(Map.of(
                    "accessToken", accessToken
                )
        );

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpirationTime)
                .sameSite("Strict")
                .build();

        httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        LocalDateTime refreshTokenExpireAt = LocalDateTime.now().plusSeconds(refreshTokenExpirationTime);
        RefreshTokenRecord newRefreshTokenRecord = RefreshTokenRecord.builder()
                .username(username)
                .tokenValue(refreshToken)
                .expireAt(refreshTokenExpireAt)
                .build();

        refreshTokenService.save(newRefreshTokenRecord);

        try {
            httpServletResponse.getWriter().println(json);
        }catch (IOException ioException){
            throw new RuntimeException(ioException);
        }
    }

    private String getRefreshToken(HttpServletRequest request){

        Cookie[] cookies = request.getCookies();

        if(cookies != null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("refreshToken")){
                    return cookie.getValue();
                }
            }
        }

        return "";
    }
}
