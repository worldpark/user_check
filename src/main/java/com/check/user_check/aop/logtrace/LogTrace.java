package com.check.user_check.aop.logtrace;

import com.check.user_check.aop.logtrace.dto.TraceStatus;

public interface LogTrace {

    TraceStatus begin(String userId, String message);

    void end(TraceStatus status);

    void exception(TraceStatus status, Exception exception);
}
