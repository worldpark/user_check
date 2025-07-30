package com.check.user_check.config.security.filter;

import com.check.user_check.config.security.CustomUserDetailsService;
import com.check.user_check.exception.token.accesstoken.AccessTokenException;
import com.check.user_check.exception.token.accesstoken.AccessTokenError;
import com.check.user_check.config.security.util.JWTUtil;
import com.check.user_check.config.security.util.AuthenticationUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request
            , HttpServletResponse response
            , FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        if(
                path.startsWith("/api/user/auth")
                || path.startsWith("/docs")
                || path.equals("/") || path.startsWith("/assets/")
                || path.equals("/api/auth/validate-token")
                || path.equals("/api/user/auth/refresh")
                || path.startsWith("/admin") || path.startsWith("/user/") //front url
                || path.startsWith("/js/") || path.startsWith("/css/")
                || path.startsWith("/ws") || path.startsWith("/wss") || path.startsWith("/app")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        String sub = null;
        try {
            Map<String, Object> payload = getAccessTokenPayload(request);
            sub = (String) payload.get("sub");
        } catch (AccessTokenException ex) {
            if (!response.isCommitted()){
                ex.sendResponseError(response);
            }
            return;
        }

        if(sub != null && SecurityContextHolder.getContext().getAuthentication() == null){
            try{
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(sub);

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails
                                , null
                                , userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails
                        (new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                filterChain.doFilter(request, response);
            }catch (UsernameNotFoundException usernameNotFoundException){
                AuthenticationUtil.sendResponseError(
                        response,
                        HttpServletResponse.SC_BAD_REQUEST,
                        "인증정보를 찾을수 없습니다.",
                        "010206"
                );
            }
        }else{
            filterChain.doFilter(request, response);
        }
    }

    private Map<String, Object> getAccessTokenPayload(HttpServletRequest httpServletRequest){
        String header = httpServletRequest.getHeader("Authorization");

        if(header == null || header.length() < 8){
            throw new AccessTokenException(AccessTokenError.UNACCEPTABLE);
        }

        String tokenType = header.substring(0, 6);
        if(!tokenType.equalsIgnoreCase("Bearer")){
            throw new AccessTokenException(AccessTokenError.BAD_TYPE);
        }

        String token = header.substring(7);

//        String token = extractAccessTokenFromCookie(httpServletRequest);
        try {
            return jwtUtil.getTokenPayload(token);
        }catch (ExpiredJwtException e) {
            throw new AccessTokenException(AccessTokenError.EXPIRED);
        } catch (MalformedJwtException e){
            throw new AccessTokenException(AccessTokenError.MALFORMED);
        } catch (SignatureException e){
            throw new AccessTokenException(AccessTokenError.BAD_SIGN);
        }
    }



    private String extractAccessTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        throw new AccessTokenException(AccessTokenError.UNACCEPTABLE);
    }
}
