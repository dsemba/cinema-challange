package com.highway.cinema.domain;

import lombok.Data;

import java.time.Duration;

@Data
public class Movie {
    private String title;
    private Duration duration;
    private boolean glassesRequired;
}
