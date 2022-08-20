package com.highway.cinema.infrastructure.mocks;

import com.highway.cinema.domain.Room;
import com.highway.cinema.domain.RoomEvent;
import com.highway.cinema.domain.dao.RoomEventRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class RoomEventRepositoryMock extends AbstractIdentifyingRepository<RoomEvent> implements RoomEventRepository {
    @Override
    public List<RoomEvent> findByRoom(Room room) {
        return entities.stream()
                .filter(x -> x.getRoom().equals(room))
                .collect(Collectors.toList());
    }

    @Override
    public boolean inSameRoomAndOverlapsWithAnyWithinTimeRange(Room room, ZonedDateTime start, ZonedDateTime end) {
        return findByRoom(room)
                .stream()
                .anyMatch(x -> x.getEnd().isAfter(start) && x.getStart().isBefore(end));
    }
}
