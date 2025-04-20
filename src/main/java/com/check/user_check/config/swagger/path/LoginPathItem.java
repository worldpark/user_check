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

public class LoginPathItem {
    public PathItem build() {

        Gson gson = new Gson();
        String requestExample = gson.toJson(
                Map.of(
                        "userId", "userId",
                        "password", "password"
                )
        );

        RequestBody requestBody =  new RequestBody()
                .content(new Content().addMediaType(
                        org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                        new MediaType().schema(new Schema<Map<String, String>>()
                                .addProperty("userId", new Schema<String>())
                                .addProperty("password", new Schema<String>())
                                .example(requestExample)
                        ))
                );

        String successResponseExample = gson.toJson(
                Map.of(
                        "message", "로그인 되었습니다.",
                        "data", Map.of(
                                "accessToken", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMj" +
                                        "M0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdC" +
                                        "I6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30",
                                "refreshToken", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMj"+
                                        "M0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdC"+
                                        "I6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30"
                        )
                )
        );

        String NotFoundResponseExample = gson.toJson(
                Map.of(
                        "code", "010104",
                        "message", "아이디 또는 비밀번호가 일치하지 않습니다."
                )
        );

        String MethodNotAllowedExample = gson.toJson(
                Map.of(
                        "code", "010109",
                        "message", "인증시 해당 메소드는 지원하지 않습니다."
                )
        );

        String BadRequestExample = gson.toJson(
                Map.of(
                        "code","010110",
                        "message","아이디 혹은 비밀번호가 누락되었습니다"
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
                        .description("아이디나 비밀번호가 잘못됨")
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
                .addApiResponse("400", new ApiResponse()
                        .description("요청 키 값이 잘못됨")
                        .content(new Content()
                                .addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                        new MediaType().schema(new Schema<Map<String, String>>()
                                                        .addProperty("code", new Schema<Integer>())
                                                        .addProperty("message", new Schema<String>())
                                                )
                                                .example(BadRequestExample)
                                )
                        )
                )
                .addApiResponse("405", new ApiResponse()
                        .description("Method 허용하지 않음.")
                        .content(new Content()
                                .addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                        new MediaType().schema(new Schema<Map<String, String>>()
                                                        .addProperty("code", new Schema<Integer>())
                                                        .addProperty("message", new Schema<String>())
                                                )
                                                .example(MethodNotAllowedExample)
                                )
                        )
                );


        return new PathItem().post(
                new Operation()
                        .tags(List.of("Auth"))
                        .summary("로그인")
                        .requestBody(requestBody)
                        .responses(apiResponses)
        );
    }
}
