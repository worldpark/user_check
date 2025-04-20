package com.check.user_check.dto;

import com.check.user_check.exception.code.BaseExceptionCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@RequiredArgsConstructor
public class ResultResponse<T> {

    private final String code;
    private final String message;
    private final T data;
    private final UUID uuid;
    private final List<UUID> uuids;

    public ResultResponse(){
        this.code = null;
        this.message = "success";
        this.data = null;
        this.uuid = null;
        this.uuids = null;
    }

    public static ResponseEntity<ResultResponse<UUID>> created(UUID id){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResultResponse.<UUID>builder()
                        .uuid(id)
                        .build()
        );
    }

    public static ResponseEntity<ResultResponse<List<UUID>>> created(List<UUID> uuids){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResultResponse.<List<UUID>>builder()
                        .uuids(uuids)
                        .build()
        );
    }

    public static ResponseEntity<ResultResponse<Void>> success(){
        return ResponseEntity.noContent().build();
    }

    public static ResponseEntity<ResultResponse<Void>> success(String message){
        return ResponseEntity.status(HttpStatus.OK).body(
                ResultResponse.<Void>builder()
                        .message(message)
                        .build()
        );
    }

    public static <T> ResponseEntity<ResultResponse<T>> successWithData(T data){
        return ResponseEntity.status(HttpStatus.OK).body(
                ResultResponse.<T>builder()
                        .data(data)
                        .build()
        );
    }

    public static ResponseEntity<ResultResponse<Void>> deleteContent(){
        return ResponseEntity.status(HttpStatus.RESET_CONTENT).body(null);
    }

    public static ResponseEntity<Object> error(
            BaseExceptionCode baseExceptionCode
            , @Nullable String... messageWithCode){

        String message = null;
        String code = null;

        if(messageWithCode != null && messageWithCode.length > 0){
            if(messageWithCode[0] == null || messageWithCode[0].isBlank()){
                message = baseExceptionCode.getError().message();
            }else{
                message = messageWithCode[0];
            }
        }else{
            message = baseExceptionCode.getError().message();
        }

        if(messageWithCode != null && messageWithCode.length > 1){
            if(messageWithCode[1] == null){
                code = baseExceptionCode.getError().code();
            }else{
                code = messageWithCode[0];
            }
        }else{
            code = baseExceptionCode.getError().code();
        }

        return ResponseEntity.status(baseExceptionCode.getError().status())
                .body(ResultResponse.<Void>builder()
                        .message(message)
                        .code(code)
                        .build()
                );
    }
}
