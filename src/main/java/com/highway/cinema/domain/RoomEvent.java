package com.highway.cinema.domain;

import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
public abstract class RoomEvent extends IdentifiableEntity {
    private final RoomEventType type;
    private final Room room;
    private final ZonedDateTime start;
    private final ZonedDateTime end;

    public RoomEvent(RoomEventType type, Room room, ZonedDateTime start, ZonedDateTime end) {
        this.type = type;
        this.room = room;
        this.start = start;
        this.end = end;
    }

    //TODO: Make sure room + scheduleAt is unique among movies
    //TODO: Make sure Seans objects don't overlap (scheduledAt and movie.duration)
}
