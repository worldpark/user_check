package com.check.user_check.exception.token.refreshToken;

import com.check.user_check.config.security.util.AuthenticationUtil;
import jakarta.servlet.http.HttpServletResponse;

public class RefreshTokenException extends RuntimeException{

    private final RefreshTokenError refreshTokenError;

    public RefreshTokenException(RefreshTokenError refreshTokenError) {
        super(refreshTokenError.name());
        this.refreshTokenError = refreshTokenError;
    }

    public void sendResponseError(HttpServletResponse httpServletResponse){
        String code = "0103";
        AuthenticationUtil.sendResponseError(
                httpServletResponse,
                refreshTokenError.getStatus().value(),
                refreshTokenError.getMessage(),
                code + refreshTokenError.getAdditionalCode()
        );
    }
}
