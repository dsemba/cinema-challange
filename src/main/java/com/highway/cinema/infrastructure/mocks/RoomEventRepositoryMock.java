package com.highway.cinema.infrastructure.mocks;

import com.highway.cinema.domain.Room;
import com.highway.cinema.domain.RoomEvent;
import com.highway.cinema.domain.dao.RoomEventRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoomEventRepositoryMock implements RoomEventRepository {
    private final List<RoomEvent> roomEvents = new ArrayList<>();

    @Override
    public List<RoomEvent> findAll() {
        return roomEvents;
    }

    @Override
    public List<RoomEvent> findByRoom(Room room) {
        return roomEvents.stream()
                .filter(x -> x.getRoom().equals(room))
                .collect(Collectors.toList());
    }

    @Override
    public boolean inSameRoomAndOverlapsWithAnyWithinTimeRange(Room room, ZonedDateTime start, ZonedDateTime end) {
        return findByRoom(room)
                .stream()
                .anyMatch(x -> x.getEnd().isAfter(start) && x.getStart().isBefore(end));
    }

    @Override
    public void save(RoomEvent roomEvent) {
        roomEvents.add(roomEvent);
    }
}
