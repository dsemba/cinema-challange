package com.highway.cinema.domain;

import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class Seans extends RoomEvent {
    private final Movie movie;

    public Seans(Movie movie, Room room, ZonedDateTime start) {
        super(RoomEventType.SEANS, room, start, start.plus(movie.getDuration()));
        this.movie = movie;
    }
}
