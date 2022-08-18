package com.highway.cinema.domain;

import java.time.ZonedDateTime;

public class RoomOutOfService extends RoomEvent {
    public RoomOutOfService(Room room, ZonedDateTime start, ZonedDateTime end) {
        super(RoomEventType.OUT_OF_SERVICE, room, start, end);
        if (start.isAfter(end)) {
            //TODO: Add custom exception
            throw new RuntimeException();
        }
    }
}
