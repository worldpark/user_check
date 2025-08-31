package com.check.user_check.service.rag;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Rrf {

    private final int k;

    public double contribution(int rank){
        return 1.0 / (k + rank);
    }
}
