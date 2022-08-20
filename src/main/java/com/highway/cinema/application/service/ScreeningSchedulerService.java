package com.highway.cinema.application.service;

import com.highway.cinema.domain.Movie;
import com.highway.cinema.domain.Room;
import com.highway.cinema.domain.seans.Seans;
import com.highway.cinema.domain.SeansSchedulingService;
import com.highway.cinema.domain.dao.MovieRepository;
import com.highway.cinema.domain.dao.RoomRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public class ScreeningSchedulerService {
    private MovieRepository movieRepository;
    private RoomRepository roomRepository;
    private SeansSchedulingService seansSchedulingService;

    public ScreeningSchedulerService(MovieRepository movieRepository, RoomRepository roomRepository, SeansSchedulingService seansSchedulingService) {
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
        this.seansSchedulingService = seansSchedulingService;
    }

    public Seans addSeans(UUID movieId, UUID roomId, ZonedDateTime startAt) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(MovieNotFoundException::new);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(RoomNotFoundException::new);

        return seansSchedulingService.scheduleSeans(movie, room, startAt);
    }


    public List<Seans> getAvailableSlots() {
        return List.of();
    }

    public List<Seans> getSeansBoard() {
        return List.of();
    }

    public List<Seans> get() {
        return List.of();
    }

    private static final class MovieNotFoundException extends RuntimeException {
    }

    private static final class RoomNotFoundException extends RuntimeException {
    }
}
