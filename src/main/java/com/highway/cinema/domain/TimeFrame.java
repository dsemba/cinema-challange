package com.highway.cinema.domain;

import lombok.Data;

@Data
public class TimeFrame {
    private static final int MAX_HOUR = 23;
    private static final int MIN_HOUR = 0;
    private int startHour;
    private int endHour;

    public TimeFrame(int startHour, int endHour) {
        if (isHourInvalid(startHour) || isHourInvalid(endHour)) {
            //TODO: Add custom error
            throw new RuntimeException();
        }
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public boolean isHourInvalid(int hour) {
        return hour < 0 || hour > 23;
    }
}
