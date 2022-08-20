package com.highway.cinema.domain;

import lombok.Getter;

@Getter
public class Room extends IdentifiableEntity{
    private final String name;
    private final int cleaningDurationInMinutes;

    public Room(String name, int cleaningDurationInMinutes) {
        this.name = name;
        this.cleaningDurationInMinutes = cleaningDurationInMinutes;
    }
}
