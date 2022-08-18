package com.highway.cinema.application.service;

import com.highway.cinema.domain.*;

import java.time.ZonedDateTime;
import java.util.List;

public class SeansSchedulingService {
    private RoomEventRepository roomEventRepository;

    public SeansSchedulingService(RoomEventRepository roomEventRepository) {
        this.roomEventRepository = roomEventRepository;
    }

    public Seans scheduleSeans(Movie movie, Room room, ZonedDateTime startAt) {
        //TODO: validate movie can be scheduled
        //TODO: schedule a movie - create Seans
        return new Seans();
    }

    public List<RoomEvent> getSeansBoard() {
        //TODO: return all room events with their types (seans, maintenance, out of service)
        return roomEventRepository.findAll();
    }
}
