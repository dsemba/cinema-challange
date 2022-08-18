package com.highway.cinema.domain.dao;

import com.highway.cinema.domain.Room;
import com.highway.cinema.domain.RoomEvent;

import java.time.ZonedDateTime;
import java.util.List;

public interface RoomEventRepository {

    List<RoomEvent> findAll();

    List<RoomEvent> findByRoom(Room room);

    //TODO: optimize on database's side
    boolean inSameRoomAndOverlapsWithAnyWithinTimeRange(Room room, ZonedDateTime start, ZonedDateTime end);

    void save(RoomEvent roomEvent);
}
