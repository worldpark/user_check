package com.check.user_check.exception.code.dto;


import lombok.Builder;

@Builder
public record ErrorResponse(int status, String code, String message) {}
