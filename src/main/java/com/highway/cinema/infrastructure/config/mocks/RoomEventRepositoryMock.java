package com.highway.cinema.infrastructure.config.mocks;

import com.highway.cinema.domain.Room;
import com.highway.cinema.domain.RoomEvent;
import com.highway.cinema.domain.RoomEventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoomEventRepositoryMock implements RoomEventRepository {
    private List<RoomEvent> roomEvents = new ArrayList<>();

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
}
