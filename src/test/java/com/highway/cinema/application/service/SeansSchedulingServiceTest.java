package com.highway.cinema.application.service;

import com.highway.cinema.domain.*;
import com.highway.cinema.domain.dao.RoomEventRepository;
import com.highway.cinema.domain.exception.OverlappingEventException;
import com.highway.cinema.domain.exception.ScheduledOutsideHoursRequiredForPremierException;
import com.highway.cinema.domain.exception.ScheduledOutsideOpeningHoursException;
import com.highway.cinema.domain.seans.Seans;
import com.highway.cinema.infrastructure.mocks.RoomEventRepositoryMock;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.*;

@Slf4j
public class SeansSchedulingServiceTest {

    @Test
    public void shouldScheduleMaintenanceAfterEverySeans() {
        RoomEventRepository roomEventRepository = new RoomEventRepositoryMock();
        SeansSchedulingService service = new SeansSchedulingService(roomEventRepository, new Settings());
        Movie movie = new Movie("Shrek", 90, false);
        Room room = new Room("Room 1", 20);
        ZonedDateTime scheduledAt = ZonedDateTime.now();

        Seans seans = service.scheduleSeans(movie, room, scheduledAt);

        Optional<RoomEvent> roomEvent = roomEventRepository.findByRoom(room)
                .stream()
                .filter(x -> x.getStart().isEqual(scheduledAt.plus(movie.getDuration())))
                .findFirst();

        assertTrue(roomEvent.isPresent());
        assertEquals(roomEvent.get().getType(), RoomEventType.MAINTENANCE);
    }

    @Test(expected = OverlappingEventException.class)
    public void shouldNotScheduleOverlappingRoomEvents() {
        RoomEventRepository roomEventRepository = new RoomEventRepositoryMock();
        SeansSchedulingService service = new SeansSchedulingService(roomEventRepository, new Settings());
        Movie movie = new Movie("Shrek", 90, false);
        Movie movie2 = new Movie("Avengers", 100, true);
        Room room = new Room("Room 1", 20);
        ZonedDateTime scheduledAt = ZonedDateTime.now();

        Seans seans = service.scheduleSeans(movie, room, scheduledAt);
        Seans seans2 = service.scheduleSeans(movie2, room, scheduledAt.plusMinutes(30));
    }

    @Test(expected = OverlappingEventException.class)
    public void shouldNotScheduleSeansWhenOverlapsWithMaintenance() {
        RoomEventRepository roomEventRepository = new RoomEventRepositoryMock();
        SeansSchedulingService service = new SeansSchedulingService(roomEventRepository, new Settings());
        Movie movie = new Movie("Shrek", 90, false);
        Movie movie2 = new Movie("Avengers", 100, true);
        Room room = new Room("Room 1", 20);
        ZonedDateTime scheduledAt = ZonedDateTime.now();

        Seans seans = service.scheduleSeans(movie, room, scheduledAt);
        Seans seans2 = service.scheduleSeans(movie2, room, scheduledAt.plus(movie.getDuration()).plusMinutes(10));
    }

    @Test
    public void shouldNotConcurrentlyScheduleSeansesAtTheSameTimeOrLocation() throws ExecutionException, InterruptedException {
        RoomEventRepository roomEventRepository = new RoomEventRepositoryMock();
        SeansSchedulingService service = new SeansSchedulingService(roomEventRepository, new Settings());
        Movie movie = new Movie("Shrek", 90, false);
        Movie movie2 = new Movie("Avengers", 100, true);
        Room room = new Room("Room 1", 20);
        ZonedDateTime scheduledAt = ZonedDateTime.now();

        ExecutorService es = Executors.newFixedThreadPool(2);
        Future<Seans> future = es.submit(() -> service.scheduleSeans(movie, room, scheduledAt));
        Future<Seans> future2 = es.submit(() -> service.scheduleSeans(movie2, room, scheduledAt.plus(movie.getDuration()).plusMinutes(10)));
        Seans seans = null, seans2;
        Throwable thrown = null;
        try {
            seans = future.get();
            seans2 = future2.get();
        } catch (ExecutionException exception) {
            thrown = exception.getCause();
        }
        assertNotNull(thrown);
        assertEquals(OverlappingEventException.class, thrown.getClass());
        assertNotNull(seans);
        assertEquals(1, roomEventRepository.findAll()
                .stream()
                .filter(x -> x.getType().equals(RoomEventType.SCREENING))
                .count());
    }

    @Ignore("Not implemented yet")
    @Test
    public void shouldNotScheduleSeansWhenRoomIsOutOfService() {
    }

    @Test(expected = ScheduledOutsideHoursRequiredForPremierException.class)
    public void shouldNotSchedulePremierOnlyOutsideDefinedTimeWindow() {
        Settings settings = new Settings();
        settings.setPremieresRequiredSchedule(new TimeFrame(17, 21));

        RoomEventRepository roomEventRepository = new RoomEventRepositoryMock();
        SeansSchedulingService service = new SeansSchedulingService(roomEventRepository, settings);
        Movie movie = new Movie("Shrek", 90, false, true);
        Room room = new Room("Room 1", 20);
        ZonedDateTime scheduledAt = ZonedDateTime.of(2011, 3, 10, 16, 59, 59, 999, ZoneId.systemDefault());

        Seans seans = service.scheduleSeans(movie, room, scheduledAt);
    }

    @Test(expected = ScheduledOutsideOpeningHoursException.class)
    public void shouldNotScheduleSeansOutsideOpeningHours() {
        Settings settings = new Settings();
        settings.setOpeningHours(new TimeFrame(8, 22));

        RoomEventRepository roomEventRepository = new RoomEventRepositoryMock();
        SeansSchedulingService service = new SeansSchedulingService(roomEventRepository, settings);
        Movie movie = new Movie("Shrek", 90, false, true);
        Room room = new Room("Room 1", 20);
        ZonedDateTime scheduledAt = ZonedDateTime.of(2011, 3, 10, 7, 59, 59, 999, ZoneId.systemDefault());

        Seans seans = service.scheduleSeans(movie, room, scheduledAt);
    }
}