package com.highway.cinema.domain;

import java.time.Duration;
import java.time.ZonedDateTime;

public class ScheduledMaintenance extends RoomEvent {
    public ScheduledMaintenance(Room room, ZonedDateTime start, Duration duration) {
        super(RoomEventType.MAINTENANCE, room, start, start.plus(duration));
    }
}
