package com.check.user_check.config.swagger.annotation.customizer;

import com.check.user_check.config.swagger.annotation.ResultRequest;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

@Component
public class ResultRequestCustomizer implements OperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        ResultRequest resultRequest = handlerMethod.getMethodAnnotation(ResultRequest.class);

        if(resultRequest != null){
            Content content = new Content();
            MediaType mediaType = new MediaType();
            Schema<?> schema = new Schema<>();
            schema.set$ref("#/components/schemas/" + resultRequest.implementation().getName());

            mediaType.setSchema(schema);
            content.addMediaType("application/json", mediaType);
            operation.getRequestBody().setContent(content);
        }

        return operation;
    }
}
