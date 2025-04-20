package com.check.user_check.aop;

import com.check.user_check.aop.logtrace.LogTrace;
import com.check.user_check.aop.logtrace.LogTraceImpl;
import com.check.user_check.aop.logtrace.LogTraceProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AopConfig {

    @Bean
    public LogTrace logTrace(){
        return new LogTraceImpl();
    }

    @Bean
    public LogTraceProxy logTraceProxy(LogTrace logTrace){
        return new LogTraceProxy(logTrace);
    }
}
