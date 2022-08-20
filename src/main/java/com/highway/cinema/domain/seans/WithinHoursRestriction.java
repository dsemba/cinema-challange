package com.highway.cinema.domain.seans;

import com.highway.cinema.domain.TimeFrame;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WithinHoursRestriction {
    private TimeFrame timeFrame;
    private Class<? extends RuntimeException> exceptionClass;
}
