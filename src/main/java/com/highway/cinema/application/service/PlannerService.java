package com.highway.cinema.application.service;

import com.highway.cinema.domain.Movie;
import com.highway.cinema.domain.Room;
import com.highway.cinema.domain.seans.Seans;
import com.highway.cinema.domain.dao.MovieRepository;
import com.highway.cinema.domain.dao.RoomRepository;
import com.highway.cinema.domain.seans.SeansScheduler;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public class PlannerService {
    private MovieRepository movieRepository;
    private RoomRepository roomRepository;
    private SeansScheduler seansScheduler;

    public PlannerService(MovieRepository movieRepository, RoomRepository roomRepository, SeansScheduler seansScheduler) {
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
        this.seansScheduler = seansScheduler;
    }

    public Seans addSeans(UUID movieId, UUID roomId, ZonedDateTime startAt) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(MovieNotFoundException::new);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(RoomNotFoundException::new);

        Seans seans = new Seans(movie, room, startAt);
        return seansScheduler.scheduleIfAvailable(seans);
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
