package com.check.user_check.config.security.filter;

import com.check.user_check.config.security.CustomUserDetailsService;
import com.check.user_check.config.security.util.JWTUtil;
import com.check.user_check.enumeratedType.TokenValid;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Map;


public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JWTUtil jwtUtil;

    public JwtChannelInterceptor(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if(StompCommand.CONNECT.equals(accessor.getCommand())){

            List<String> authorization = accessor.getNativeHeader("Authorization");

            if(authorization != null && !authorization.isEmpty()){
                String token = authorization.get(0).replace("Bearer", "");

                if(jwtUtil.validateToken(token).equals(TokenValid.CORRECT)){

                    Map<String, Object> tokenPayload = jwtUtil.getTokenPayload(token);

                    String username = (String) tokenPayload.get("sub");

                    Authentication auth = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                    );

                    accessor.setUser(auth);
                }
            }
        }

        return message;
    }
}
