package com.highway.cinema.domain;

import lombok.Data;

@Data
public class Room {
    private String name;

    public Room(String name) {
        this.name = name;
    }
}
