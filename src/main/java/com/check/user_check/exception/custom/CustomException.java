package com.check.user_check.exception.custom;

import com.check.user_check.exception.code.BaseExceptionCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private final BaseExceptionCode baseExceptionCode;

    public CustomException(BaseExceptionCode baseExceptionCode){
        super(baseExceptionCode.getError().message());
        this.baseExceptionCode = baseExceptionCode;
    }

}
