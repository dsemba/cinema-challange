package com.highway.cinema.domain.seans;

import com.highway.cinema.domain.Movie;
import com.highway.cinema.domain.Room;
import com.highway.cinema.domain.RoomEvent;
import com.highway.cinema.domain.RoomEventType;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public
class MovieScreening extends RoomEvent {
    private final Movie movie;

    public MovieScreening(Movie movie, Room room, ZonedDateTime start) {
        super(RoomEventType.SCREENING, room, start, start.plus(movie.getDuration()));
        this.movie = movie;
    }
}
