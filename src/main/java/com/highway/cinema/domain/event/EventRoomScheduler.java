package com.highway.cinema.domain.event;

import com.highway.cinema.domain.RoomEvent;
import com.highway.cinema.domain.dao.RoomEventRepository;
import com.highway.cinema.domain.seans.exception.OverlappingEventException;

import java.util.List;

public class EventRoomScheduler {
    private final RoomEventRepository roomEventRepository;

    public EventRoomScheduler(RoomEventRepository roomEventRepository) {
        this.roomEventRepository = roomEventRepository;
    }

    public synchronized void validateAndSchedule(List<RoomEvent> roomEvents) {
        validate(roomEvents);
        roomEvents.forEach(roomEventRepository::save);
    }

    private void validate(List<RoomEvent> roomEvents) {
        for (RoomEvent roomEvent : roomEvents) {
            if (overlapsWithOtherEvents(roomEvent)) {
                throw new OverlappingEventException();
            }
        }
    }

    private boolean overlapsWithOtherEvents(RoomEvent roomEvent) {
        return roomEventRepository
                .inSameRoomAndOverlapsWithAnyWithinTimeRange(roomEvent.getRoom(), roomEvent.getStart(), roomEvent.getEnd());
    }
}
