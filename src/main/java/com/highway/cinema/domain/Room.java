package com.highway.cinema.domain;

import lombok.Getter;

@Getter
public class Room {
    private final String name;
    private final int cleaningDurationInMinutes;

    public Room(String name, int cleaningDurationInMinutes) {
        this.name = name;
        this.cleaningDurationInMinutes = cleaningDurationInMinutes;
    }
}
