package com.check.user_check.aop.logtrace;

import com.check.user_check.aop.logtrace.dto.TraceId;
import com.check.user_check.aop.logtrace.dto.TraceStatus;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LogTraceImpl implements LogTrace{

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();

    @Override
    public TraceStatus begin(String userId, String message) {

        TraceId traceId = traceIdHolder.get();

        if(traceId == null){
            traceIdHolder.set(new TraceId(userId));
        }else{
            traceIdHolder.set(traceId.createNextLevel());
        }

        traceId = traceIdHolder.get();
        Long startTimeMs = System.currentTimeMillis();

        log.info("[{}] {}{}", traceId.getUserId(), addSpace(START_PREFIX, traceId.getLevel()), message);

        return new TraceStatus(traceId, startTimeMs, message);
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception exception) {
        complete(status, exception);
    }

    private void complete(TraceStatus status, Exception exception){
        Long stopTimeMs = System.currentTimeMillis();
        Long resultTimeMs = stopTimeMs - status.startTimeMs();

        TraceId traceId = status.traceId();

        if(exception == null){
            log.info("[{}] {}{} time={}ms"
                    , traceId.getUserId()
                    , addSpace(COMPLETE_PREFIX, traceId.getLevel())
                    , status.message()
                    , resultTimeMs);
        }else{
            log.info("[{}] {}{} time={}ms ex={}"
                    , traceId.getUserId()
                    , addSpace(EX_PREFIX, traceId.getLevel())
                    , status.message()
                    , resultTimeMs
                    , exception.toString());
        }

        traceId = traceIdHolder.get();

        if (traceId.isFirstLevel()) {
            traceIdHolder.remove();//destroy
        } else {
            traceIdHolder.set(traceId.createPreviousLevel());
        }
    }

    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append( (i == level - 1) ? "|" + prefix : "|   ");
        }
        return sb.toString();
    }
}
