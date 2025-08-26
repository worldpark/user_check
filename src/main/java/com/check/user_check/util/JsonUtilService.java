package com.check.user_check.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonUtilService {

    public static String toJsonString(Object object){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {
            String message = objectMapper.writeValueAsString(object);
            return message;

        }catch (JsonProcessingException exception){
            throw new RuntimeException(exception);
        }
    }
}
