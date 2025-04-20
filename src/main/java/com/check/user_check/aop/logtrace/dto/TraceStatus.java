package com.check.user_check.aop.logtrace.dto;


public record TraceStatus(TraceId traceId, Long startTimeMs, String message) {}
