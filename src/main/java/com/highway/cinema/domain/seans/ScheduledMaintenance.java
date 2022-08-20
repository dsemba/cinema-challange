package com.highway.cinema.domain.seans;

import com.highway.cinema.domain.Room;
import com.highway.cinema.domain.RoomEvent;
import com.highway.cinema.domain.RoomEventType;

import java.time.ZonedDateTime;

class ScheduledMaintenance extends RoomEvent {
    public ScheduledMaintenance(Room room, ZonedDateTime start) {
        super(RoomEventType.MAINTENANCE, room, start, start.plusMinutes(room.getCleaningDurationInMinutes()));
    }
}
