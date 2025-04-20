package com.check.user_check.config.swagger.annotation.customizer;

import com.check.user_check.config.swagger.annotation.ResultCreatedResponse;
import com.check.user_check.util.UUIDv6Generator;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.UUID;

@Component
public class ResultCreatedResponseCustomizer implements OperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        ResultCreatedResponse resultCreatedResponse = handlerMethod.getMethodAnnotation(ResultCreatedResponse.class);

        if(resultCreatedResponse != null){
            if(operation.getResponses() == null){
                operation.responses(new ApiResponses());
            }

            operation.getResponses().remove("200");

            ResponseEntity<com.check.user_check.dto.ResultResponse<UUID>> idResponse =
                    com.check.user_check.dto.ResultResponse.created(UUIDv6Generator.generate());

            ApiResponse createResponse = getResultResponse(resultCreatedResponse, idResponse);
            operation.getResponses().addApiResponse("201", createResponse);
        }

        return operation;
    }

    private ApiResponse getResultResponse(
            ResultCreatedResponse resultCreatedResponse,
            ResponseEntity<?> responseEntity
    ){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setDescription(resultCreatedResponse.description());

        Content content = new Content();
        MediaType mediaType = new MediaType();

        Schema<?> schema = new Schema<>();

        schema.setType("object");
        schema.setAdditionalProperties(false);
        schema.setExample(responseEntity.getBody());

        mediaType.setSchema(schema);
        content.addMediaType("application/json", mediaType);
        apiResponse.setContent(content);

        return apiResponse;

    }
}
