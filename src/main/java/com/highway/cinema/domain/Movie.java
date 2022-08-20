package com.highway.cinema.domain;

import lombok.Getter;

import java.time.Duration;

@Getter
public class Movie extends IdentifiableEntity {
    private String title;
    private Duration duration;
    private boolean glassesRequired;
    private boolean isPremier;

    public Movie(String title, int durationInMinutes, boolean glassesRequired) {
        this.title = title;
        this.duration = Duration.ofMinutes(durationInMinutes);
        this.glassesRequired = glassesRequired;
    }

    public Movie(String title, int durationInMinutes, boolean glassesRequired, boolean isPremier) {
        this(title, durationInMinutes, glassesRequired);
        this.isPremier = isPremier;
    }
}
