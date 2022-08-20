package com.highway.cinema.domain.dao;

import com.highway.cinema.domain.Room;
import com.highway.cinema.domain.RoomEvent;

import java.time.ZonedDateTime;
import java.util.List;

public interface RoomEventRepository extends Repository<RoomEvent> {
    List<RoomEvent> findByRoom(Room room);

    //TODO: optimize on database's side
    boolean inSameRoomAndOverlapsWithAnyWithinTimeRange(Room room, ZonedDateTime start, ZonedDateTime end);
}
