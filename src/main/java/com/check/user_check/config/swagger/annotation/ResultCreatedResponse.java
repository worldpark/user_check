package com.check.user_check.config.swagger.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResultCreatedResponse {

    String description() default "생성 성공";
}
