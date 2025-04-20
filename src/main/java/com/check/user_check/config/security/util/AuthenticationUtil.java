package com.check.user_check.config.security.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

@RequiredArgsConstructor
public class AuthenticationUtil {

    public static int accessTokenExpirationTime = 60;

    public static void sendResponseError(
            HttpServletResponse httpServletResponse
            , int statusCode
            , String message
            , String code
    ){
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(statusCode);

        Gson gson = new Gson();

        String json = gson.toJson(
                Map.of(
                        "code", code,
                        "message", message
                )
        );

        try {
            httpServletResponse.getWriter().println(json);
        }catch (IOException ioException){
            throw new RuntimeException(ioException);
        }
    }

    public static Map<String, String> parseRequestJSON(HttpServletRequest request)
            throws RuntimeException {
        try (Reader reader = new InputStreamReader(request.getInputStream())){
            Gson gson = new Gson();

            return  gson.fromJson(reader, Map.class);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
