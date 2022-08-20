package com.highway.cinema.application.service;

import com.highway.cinema.domain.*;
import com.highway.cinema.domain.dao.MovieRepository;
import com.highway.cinema.domain.dao.RoomEventRepository;
import com.highway.cinema.domain.dao.RoomRepository;
import com.highway.cinema.infrastructure.mocks.MovieRepositoryMock;
import com.highway.cinema.infrastructure.mocks.RoomEventRepositoryMock;
import com.highway.cinema.infrastructure.mocks.RoomRepositoryMock;
import org.junit.Test;

import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ScreeningSchedulerServiceTest {

    @Test
    public void shouldReturnProperSeans() {
        //given
        MovieRepository movieRepository = new MovieRepositoryMock();
        RoomRepository roomRepository = new RoomRepositoryMock();
        RoomEventRepository roomEventRepository = new RoomEventRepositoryMock();
        SeansSchedulingService seansSchedulingService = new SeansSchedulingService(roomEventRepository, new Settings());
        ScreeningSchedulerService screeningSchedulerService = new ScreeningSchedulerService(movieRepository, roomRepository, seansSchedulingService);
        //when
        Movie movie = new Movie("Shrek", 90);
        Room room = new Room("Room 1", 20);
        movieRepository.save(movie);
        roomRepository.save(room);
        ZonedDateTime scheduledAt = ZonedDateTime.now();
        Seans seans = screeningSchedulerService.addSeans(movie.getUuid(), room.getUuid(), scheduledAt);
        //then
        assertNotNull(seans);
        assertEquals(movie, seans.getMovie());
        assertEquals(room, seans.getRoom());
        assertEquals(scheduledAt, seans.getStart());
        assertEquals(scheduledAt.plusMinutes(movie.getDuration().toMinutes()), seans.getEnd());
    }
}