package com.check.user_check.config.swagger.annotation.customizer;

import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.http.ResponseEntity;

public class ResultCreator {

    protected static ApiResponse getResultResponse(
            String description,
            ResponseEntity<?> responseEntity
    ){
        ApiResponse response = new ApiResponse();

        if(description != null){
            response.setDescription(description);
        }

        Content content = new Content();
        MediaType mediaType = new MediaType();

        Schema<?> schema = new Schema<>();

        schema.setType("object");
        schema.setAdditionalProperties(false);

        if(responseEntity != null){
            schema.setExample(responseEntity.getBody());
        }

        mediaType.setSchema(schema);
        content.addMediaType("application/json", mediaType);
        response.setContent(content);

        return response;
    }
}
