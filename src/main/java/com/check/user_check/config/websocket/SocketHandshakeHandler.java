package com.check.user_check.config.websocket;

import com.check.user_check.config.security.util.JWTUtil;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocketHandshakeHandler extends DefaultHandshakeHandler {

    private final JWTUtil jwtUtil;

    public SocketHandshakeHandler(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected Principal determineUser(
            ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {

        String token = urlParamToMap(request).get("token").get(0);

        Map<String, Object> tokenPayload = jwtUtil.getTokenPayload(token);

        String username = (String) tokenPayload.get("sub");

        return () -> username;
    }

    private Map<String, List<String>> urlParamToMap(ServerHttpRequest request){
        String param = request.getURI().getRawQuery();

        Map<String, List<String>> queryParams = new HashMap<>();
        if (param != null) {
            String[] pairs = param.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf('=');
                String key = idx > 0 ? decode(pair.substring(0, idx)) : decode(pair);
                String value = idx > 0 && pair.length() > idx + 1 ? decode(pair.substring(idx + 1)) : "";

                queryParams.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
            }
        }

        return queryParams;
    }

    private String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
