package com.check.user_check.config.swagger.annotation.customizer;

import com.check.user_check.config.swagger.annotation.ResultUpdateAndDeleteResponse;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

@Component
public class ResultUpdateAndDeleteResponseCustomizer implements OperationCustomizer {


    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {

        ResultUpdateAndDeleteResponse annotation = handlerMethod.getMethodAnnotation(ResultUpdateAndDeleteResponse.class);

        if(annotation != null){

            if(operation.getResponses() == null){
                operation.responses(new ApiResponses());
            }

            operation.getResponses().remove("200");

            ApiResponse apiResponse = ResultCreator.getResultResponse(null, null);
            operation.getResponses().addApiResponse("200", apiResponse);
        }

        return operation;
    }
}
