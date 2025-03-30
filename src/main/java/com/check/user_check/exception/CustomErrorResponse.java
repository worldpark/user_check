package com.check.user_check.exception;

import lombok.Builder;

@Builder
public record CustomErrorResponse(String errorCode, String errorMessage) {}
