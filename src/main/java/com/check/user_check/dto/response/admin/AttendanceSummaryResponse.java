package com.check.user_check.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public record AttendanceSummaryResponse(
        SummaryData totalData,
        SummaryData presentData,
        SummaryData absentData
) {

    @RequiredArgsConstructor
    @Getter
    public static class SummaryData{
        private final String color;
        private final String name;
        private long value = 0;

        public void setValue(long value) {
            this.value = value;
        }
    }
}
