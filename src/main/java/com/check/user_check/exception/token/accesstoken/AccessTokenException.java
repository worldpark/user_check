package com.check.user_check.exception.token.accesstoken;


import com.check.user_check.config.security.util.AuthenticationUtil;
import jakarta.servlet.http.HttpServletResponse;

public class AccessTokenException extends RuntimeException{

    private final AccessTokenError accessTokenError;

    public AccessTokenException(AccessTokenError accessTokenError){
        super(accessTokenError.name());
        this.accessTokenError = accessTokenError;
    }

    public void sendResponseError(HttpServletResponse httpServletResponse){
        String code = "0102";

        AuthenticationUtil.sendResponseError(
                httpServletResponse,
                accessTokenError.getStatus().value(),
                accessTokenError.getMessage(),
                code + accessTokenError.getAdditionalCode()
        );
    }
}
