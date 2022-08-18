package com.highway.cinema.infrastructure.config;

import com.highway.cinema.domain.Room;
import com.highway.cinema.domain.TimeFrame;

import java.util.ArrayList;
import java.util.List;

public class Config {

    private static final TimeFrame openingHours = new TimeFrame(8, 22);
    private static final List<Room> availableRooms = new ArrayList<>();

    static {
        availableRooms.add(new Room("Room 1"));
        availableRooms.add(new Room("Room 2"));
        availableRooms.add(new Room("Room 3"));
    }
}
