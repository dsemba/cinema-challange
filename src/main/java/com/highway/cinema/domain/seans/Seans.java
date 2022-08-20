package com.highway.cinema.domain.seans;

import com.highway.cinema.domain.Movie;
import com.highway.cinema.domain.Room;
import com.highway.cinema.domain.seans.MovieScreening;
import com.highway.cinema.domain.seans.ScheduledMaintenance;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class Seans {
    private final MovieScreening screening;
    private final ScheduledMaintenance maintenance;

    public Seans(Movie movie, Room room, ZonedDateTime start) {
        this.screening = new MovieScreening(movie, room, start);
        this.maintenance = new ScheduledMaintenance(room, start.plus(movie.getDuration()));
    }
}
