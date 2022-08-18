package com.highway.cinema.domain.dao;

import com.highway.cinema.domain.Room;
import com.highway.cinema.domain.RoomEvent;

import java.util.List;

public interface RoomEventRepository {

    List<RoomEvent> findAll();

    List<RoomEvent> findByRoom(Room room);

    void save(RoomEvent roomEvent);
}
