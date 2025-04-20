package com.check.user_check.config.swagger.annotation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@RequestBody(content = @Content(schema = @Schema()))
public @interface ResultRequest {
    Class<?> implementation() default Void.class;
}
