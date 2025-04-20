package com.check.user_check.aop.logtrace;

import com.check.user_check.aop.logtrace.annotation.NoLog;
import com.check.user_check.aop.logtrace.dto.TraceStatus;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Method;

@Aspect
@RequiredArgsConstructor
public class LogTraceProxy {

    private final LogTrace logTrace;

    @Around("""
            (execution(* com.check.user_check.controller..*(..))
                || execution(* com.check.user_check.service..*(..))
                || execution(* com.check.user_check.repository..*(..)))
            """)
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable{

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        if(method.isAnnotationPresent(NoLog.class)){
            return joinPoint.proceed();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TraceStatus status = null;

        try{
            String userId = "";

            if(authentication != null && authentication.isAuthenticated()){
                userId = authentication.getName();
            }

            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(userId, message);

            Object result = joinPoint.proceed();

            logTrace.end(status);
            return result;
        }catch (Exception exception){
            logTrace.exception(status, exception);
            throw exception;
        }
    }
}
