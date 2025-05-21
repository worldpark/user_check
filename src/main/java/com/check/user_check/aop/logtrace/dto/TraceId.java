package com.check.user_check.aop.logtrace.dto;

import lombok.Getter;

@Getter
public class TraceId {

    private String userId;
    private int level;

    public TraceId(){
        this.userId = "Anonymous";
        this.level = 0;
    }

    public TraceId(String userId){
        this.userId = userId;
        this.level = 0;
    }

    public TraceId(String userId, int level) {
        this.userId = userId;
        this.level = level;
    }

    public TraceId createNextLevel(){
        return new TraceId(this.userId, this.level + 1);
    }

    public TraceId createPreviousLevel(){
        return new TraceId(this.userId, this.level - 1);
    }

    public boolean isFirstLevel(){
        return this.level == 0;
    }
}
