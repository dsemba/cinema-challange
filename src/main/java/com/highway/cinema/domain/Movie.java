package com.highway.cinema.domain;

import lombok.Getter;

import java.time.Duration;

@Getter
public class Movie extends IdentifiableEntity {
    private String title;
    private Duration duration;
    private boolean glassesRequired;
    private boolean isPremier;

    public Movie(String title, int durationInMinutes) {
        this(title, durationInMinutes, false);
    }

    public Movie(String title, int durationInMinutes, boolean glassesRequired) {
        this(title, durationInMinutes, glassesRequired, false);
    }

    public Movie(String title, int durationInMinutes, boolean glassesRequired, boolean isPremier) {
        this.title = title;
        this.duration = Duration.ofMinutes(durationInMinutes);
        this.glassesRequired = glassesRequired;
        this.isPremier = isPremier;
    }
}
