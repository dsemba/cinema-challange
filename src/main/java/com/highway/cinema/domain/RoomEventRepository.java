package com.highway.cinema.domain;

import java.util.List;

public interface RoomEventRepository {

    List<RoomEvent> findAll();

    List<RoomEvent> findByRoom(Room room);
}
