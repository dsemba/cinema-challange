package com.highway.cinema.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class Premier extends Movie {
    private TimeFrame requiresSchedulingAt;
}
