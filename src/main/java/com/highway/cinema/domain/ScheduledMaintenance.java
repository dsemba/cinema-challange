package com.highway.cinema.domain;

import java.time.ZonedDateTime;

public class ScheduledMaintenance extends RoomEvent {
    public ScheduledMaintenance(Room room, ZonedDateTime start) {
        super(RoomEventType.MAINTENANCE, room, start, start.plusMinutes(room.getCleaningDurationInMinutes()));
    }
}
