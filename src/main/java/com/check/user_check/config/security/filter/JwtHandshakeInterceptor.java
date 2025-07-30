package com.check.user_check.config.security.filter;

import com.check.user_check.config.security.util.JWTUtil;
import com.check.user_check.enumeratedType.TokenValid;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JWTUtil jwtUtil;

    public JwtHandshakeInterceptor(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if(request instanceof ServletServerHttpRequest servletServerHttpRequest){

            String token = servletServerHttpRequest.getServletRequest().getParameter("token");

            if(token != null && jwtUtil.validateToken(token).equals(TokenValid.CORRECT)){

                Map<String, Object> tokenPayload = jwtUtil.getTokenPayload(token);
                String username = (String) tokenPayload.get("sub");
                attributes.put("username", username);

                return true;
            }

        }

        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
