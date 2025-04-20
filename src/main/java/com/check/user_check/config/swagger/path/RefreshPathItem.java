package com.check.user_check.config.swagger.path;

import com.google.gson.Gson;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

import java.util.List;
import java.util.Map;

public class RefreshPathItem {

    public PathItem build() {

        Gson gson = new Gson();
        String requestExample = gson.toJson(
                Map.of(
                        "accessToken", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMj" +
                                "M0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdC" +
                                "I6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30",
                        "refreshToken", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMj"+
                                "M0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdC"+
                                "I6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30"
                )
        );

        RequestBody requestBody =  new RequestBody()
                .content(new Content().addMediaType(
                        org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                        new MediaType().schema(new Schema<Map<String, String>>()
                                .addProperty("accessToken", new Schema<String>())
                                .addProperty("refreshToken", new Schema<String>())
                                .example(requestExample)
                        ))
                );

        String successResponseExample = gson.toJson(
                Map.of(
                        "accessToken", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMj" +
                                "M0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdC" +
                                "I6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30",
                        "refreshToken", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMj"+
                                "M0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdC"+
                                "I6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30"
                )
        );

        String NotFoundResponseExample = gson.toJson(
                Map.of(
                        "code", "010301",
                        "message", "인증정보가 없습니다."
                )
        );

        String renewalTokenIsExpired = gson.toJson(
                Map.of(
                        "code", "010305",
                        "message", "갱신 정보가 만료되었습니다."
                )
        );

        ApiResponses apiResponses = new ApiResponses()
                .addApiResponse("200", new ApiResponse()
                        .description("OK")
                        .content(new Content()
                                .addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                        new MediaType().schema(new Schema<Map<String, Object>>()
                                                .addProperty("message", new Schema<String>())
                                                .addProperty("data", new Schema<Map<String, String>>()
                                                        .addProperty("accessToken", new Schema<String>())
                                                        .addProperty("refreshToken", new Schema<String>())
                                                )
                                                .example(successResponseExample)
                                        )
                                )
                        )
                )
                .addApiResponse("400", new ApiResponse()
                        .description("토큰이 입력되지 않거나 잘못됨")
                        .content(new Content()
                                .addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                        new MediaType().schema(new Schema<Map<String, String>>()
                                                        .addProperty("code", new Schema<Integer>())
                                                        .addProperty("message", new Schema<String>())
                                                )
                                                .example(NotFoundResponseExample)
                                )
                        )
                )
                .addApiResponse("403", new ApiResponse()
                        .description("갱신정보 만료")
                        .content(new Content()
                                .addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                        new MediaType().schema(new Schema<Map<String, String>>()
                                                        .addProperty("code", new Schema<Integer>())
                                                        .addProperty("message", new Schema<String>())
                                                )
                                                .example(renewalTokenIsExpired)
                                )
                        )
                );


        return new PathItem().post(
                new Operation()
                        .tags(List.of("Auth"))
                        .summary("리프레쉬 토큰 발급")
                        .requestBody(requestBody)
                        .responses(apiResponses)
        );
    }
}
