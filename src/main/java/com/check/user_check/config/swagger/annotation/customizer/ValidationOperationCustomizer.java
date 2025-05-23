package com.check.user_check.config.swagger.annotation.customizer;

import com.check.user_check.exception.code.ClientExceptionCode;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Component
public class ValidationOperationCustomizer implements OperationCustomizer {

    private static final Map<Class<? extends Annotation>, String>
            VALIDATION_DESCRIPTIONS = new HashMap<>();

    static {
        VALIDATION_DESCRIPTIONS.put(NotBlank.class, "유효성 검사 오류 (NotBlank)");
        VALIDATION_DESCRIPTIONS.put(Size.class, "유효성 검사 오류 (Size)");
        VALIDATION_DESCRIPTIONS.put(NotNull.class, "유효성 검사 오류 (Not Null)");
        VALIDATION_DESCRIPTIONS.put(Positive.class, "유효성 검사 오류 (Positive)");
    }

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        if (operation.getResponses() == null) {
            operation.responses(new io.swagger.v3.oas.models.responses.ApiResponses());
        }

        for (MethodParameter parameter : handlerMethod.getMethodParameters()) {
            for (Field field : parameter.getParameter().getType().getDeclaredFields()) {
                int entiryIndex = 1;
                for (Map.Entry<Class<? extends Annotation>, String> entry:
                        VALIDATION_DESCRIPTIONS.entrySet()
                ) {
                    if (field.isAnnotationPresent(entry.getKey())) {
                        try {
                            ApiResponse apiResponse = createApiResponse(
                                    field, entry.getKey(), entry.getValue()
                            );
                            operation.getResponses().addApiResponse(
                                    "400 ("+ field.getName() + " " + entiryIndex + ")",
                                    apiResponse
                            );

                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                    }
                    entiryIndex++;
                }
            }
        }

        return operation;
    }

    private ApiResponse createApiResponse(
            Field field, Class<? extends Annotation> annotationClass, String description
    ) throws Throwable {
        Annotation annotation = field.getAnnotation(annotationClass);
        String defaultMessage = (String) java.lang.reflect.Proxy.getInvocationHandler(annotation)
                .invoke(annotation, annotationClass.getMethod("message"), null);
        String message = defaultMessage.contains("|")
                ? defaultMessage.split("\\|")[0] : defaultMessage;
        String code = defaultMessage.contains("|")
                ? defaultMessage.split("\\|")[1] : null;

        ResponseEntity<Object> response = com.check.user_check.dto.ResultResponse
                .validation(ClientExceptionCode.BAD_REQUEST, field.getName(), message, code);

        return new ApiResponse()
                .description(description)
                .content(new io.swagger.v3.oas.models.media.Content().addMediaType(
                        MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().schema(
                                new Schema<>().type("object").example(response.getBody())
                        )));
    }
}