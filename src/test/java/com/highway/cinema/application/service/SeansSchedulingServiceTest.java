package com.highway.cinema.application.service;

import com.highway.cinema.domain.*;
import com.highway.cinema.infrastructure.config.mocks.RoomEventRepositoryMock;
import junit.framework.TestCase;

import java.time.ZonedDateTime;
import java.util.Optional;

public class SeansSchedulingServiceTest extends TestCase {

    public void testScheduleSeans() {
        RoomEventRepository roomEventRepository = new RoomEventRepositoryMock();
        SeansSchedulingService service = new SeansSchedulingService(roomEventRepository);
        Movie movie = new Movie();
        Room room = new Room("Room 1");
        ZonedDateTime scheduledAt = ZonedDateTime.now();

        Seans seans = service.scheduleSeans(movie, room, scheduledAt);

        Optional<RoomEvent> roomEvent = roomEventRepository.findByRoom(room)
                .stream()
                .filter(x -> x.getStart().isEqual(scheduledAt.plus(movie.getDuration())))
                .findFirst();

        assertTrue(roomEvent.isPresent());
        assertEquals(roomEvent.get().getType(), RoomEventType.MAINTENANCE);
    }
}