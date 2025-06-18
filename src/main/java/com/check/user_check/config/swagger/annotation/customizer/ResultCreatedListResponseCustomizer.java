package com.check.user_check.config.swagger.annotation.customizer;

import com.check.user_check.config.swagger.annotation.ResultCreatedListResponse;
import com.check.user_check.dto.ResultResponse;
import com.check.user_check.util.UUIDv6Generator;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.List;
import java.util.UUID;

@Component
public class ResultCreatedListResponseCustomizer implements OperationCustomizer {


    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {

        ResultCreatedListResponse annotation = handlerMethod.getMethodAnnotation(
                ResultCreatedListResponse.class
        );

        if(annotation != null){
            if(operation.getResponses() == null){
                operation.responses(new ApiResponses());
            }

            operation.getResponses().remove("200");

            ResponseEntity<ResultResponse<List<UUID>>> response =
                    ResultResponse.created(List.of(UUIDv6Generator.generate(), UUIDv6Generator.generate()));

            ApiResponse apiResponse = ResultCreator.getResultResponse(annotation.description(), response);

            operation.getResponses().addApiResponse("201", apiResponse);
        }

        return operation;
    }
}
