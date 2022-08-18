package com.highway.cinema.application.service;

import com.highway.cinema.domain.*;
import com.highway.cinema.domain.dao.RoomEventRepository;
import com.highway.cinema.domain.Settings;
import com.highway.cinema.infrastructure.mocks.RoomEventRepositoryMock;
import org.junit.Ignore;
import org.junit.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @Test(expected = SeansSchedulingService.OverlappingEventException.class)
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

    @Test(expected = SeansSchedulingService.OverlappingEventException.class)
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

    @Ignore("Not implemented yet")
    @Test
    public void shouldNotConcurrentlyScheduleSeansesAtTheSameTimeOrLocation() {
    }

    @Ignore("Not implemented yet")
    @Test
    public void shouldNotScheduleSeansWhenRoomIsOutOfService() {
    }

    @Test(expected = SeansSchedulingService.ScheduledOutsideHoursRequiredForPremierException.class)
    public void shouldNotSchedulePremierOnlyOutsideDefinedTimeWindow() {
        Settings settings = new Settings();
        settings.setPremieresRequiredSchedule(new TimeFrame(17, 21));

        RoomEventRepository roomEventRepository = new RoomEventRepositoryMock();
        SeansSchedulingService service = new SeansSchedulingService(roomEventRepository, settings);
        Movie movie = new Movie("Shrek", 90, false, true);
        Room room = new Room("Room 1", 20);
        ZonedDateTime scheduledAt = ZonedDateTime.of(2011, 3, 10, 16, 59, 59,999, ZoneId.systemDefault());

        Seans seans = service.scheduleSeans(movie, room, scheduledAt);
    }

    @Test(expected = SeansSchedulingService.ScheduledOutsideOpeningHoursException.class)
    public void shouldNotScheduleSeansOutsideOpeningHours() {
        Settings settings = new Settings();
        settings.setOpeningHours(new TimeFrame(8, 22));

        RoomEventRepository roomEventRepository = new RoomEventRepositoryMock();
        SeansSchedulingService service = new SeansSchedulingService(roomEventRepository, settings);
        Movie movie = new Movie("Shrek", 90, false, true);
        Room room = new Room("Room 1", 20);
        ZonedDateTime scheduledAt = ZonedDateTime.of(2011, 3, 10, 7, 59, 59,999, ZoneId.systemDefault());

        Seans seans = service.scheduleSeans(movie, room, scheduledAt);
    }
}