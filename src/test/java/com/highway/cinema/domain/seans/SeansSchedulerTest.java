package com.highway.cinema.domain.seans;

import com.highway.cinema.domain.*;
import com.highway.cinema.domain.dao.RoomEventRepository;
import com.highway.cinema.domain.event.EventRoomScheduler;
import com.highway.cinema.domain.seans.SeansScheduler;
import com.highway.cinema.domain.seans.Settings;
import com.highway.cinema.domain.seans.exception.OverlappingEventException;
import com.highway.cinema.domain.seans.exception.ScheduledOutsideHoursRequiredForPremierException;
import com.highway.cinema.domain.seans.exception.ScheduledOutsideOpeningHoursException;
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
public class SeansSchedulerTest {

    @Test
    public void shouldScheduleMaintenanceAfterEverySeans() {
        //given
        RoomEventRepository roomEventRepository = new RoomEventRepositoryMock();
        EventRoomScheduler eventRoomScheduler = new EventRoomScheduler(roomEventRepository);
        SeansScheduler seansScheduler = new SeansScheduler(eventRoomScheduler, new Settings());
        Movie movie = new Movie("Shrek", 90, false);
        Room room = new Room("Room 1", 20);
        ZonedDateTime scheduledAt = ZonedDateTime.now();

        //when
        Seans seans = new Seans(movie, room, scheduledAt);
        seansScheduler.scheduleIfAvailable(seans);

        //then
        Optional<RoomEvent> roomEvent = roomEventRepository.findByRoom(room)
                .stream()
                .filter(x -> x.getStart().isEqual(scheduledAt.plus(movie.getDuration())))
                .findFirst();
        assertTrue(roomEvent.isPresent());
        assertEquals(roomEvent.get().getType(), RoomEventType.MAINTENANCE);
    }

    @Test(expected = OverlappingEventException.class)
    public void shouldNotScheduleOverlappingRoomEvents() {
        //given
        RoomEventRepository roomEventRepository = new RoomEventRepositoryMock();
        EventRoomScheduler eventRoomScheduler = new EventRoomScheduler(roomEventRepository);
        SeansScheduler seansScheduler = new SeansScheduler(eventRoomScheduler, new Settings());
        Movie movie = new Movie("Shrek", 90, false);
        Movie movie2 = new Movie("Avengers", 100, true);
        Room room = new Room("Room 1", 20);
        ZonedDateTime scheduledAt = ZonedDateTime.now();

        //when
        Seans seans = schedule(seansScheduler, movie, room, scheduledAt);
        Seans seans2 = schedule(seansScheduler, movie2, room, scheduledAt.plusMinutes(30));
    }

    @Test(expected = OverlappingEventException.class)
    public void shouldNotScheduleSeansWhenOverlapsWithMaintenance() {
        //given
        RoomEventRepository roomEventRepository = new RoomEventRepositoryMock();
        EventRoomScheduler eventRoomScheduler = new EventRoomScheduler(roomEventRepository);
        SeansScheduler seansScheduler = new SeansScheduler(eventRoomScheduler, new Settings());
        Movie movie = new Movie("Shrek", 90, false);
        Movie movie2 = new Movie("Avengers", 100, true);
        Room room = new Room("Room 1", 20);
        ZonedDateTime scheduledAt = ZonedDateTime.now();

        //when
        Seans seans = schedule(seansScheduler, movie, room, scheduledAt);
        Seans seans2 = schedule(seansScheduler, movie2, room, scheduledAt.plus(movie.getDuration()).plusMinutes(10));
    }

    @Test
    public void shouldNotConcurrentlyScheduleSeansesAtTheSameTimeOrLocation() throws ExecutionException, InterruptedException {
        //given
        RoomEventRepository roomEventRepository = new RoomEventRepositoryMock();
        EventRoomScheduler eventRoomScheduler = new EventRoomScheduler(roomEventRepository);
        SeansScheduler seansScheduler = new SeansScheduler(eventRoomScheduler, new Settings());
        Movie movie = new Movie("Shrek", 90, false);
        Movie movie2 = new Movie("Avengers", 100, true);
        Room room = new Room("Room 1", 20);
        ZonedDateTime scheduledAt = ZonedDateTime.now();

        //when
        ExecutorService es = Executors.newFixedThreadPool(2);
        Future<Seans> future = es.submit(() -> schedule(seansScheduler, movie, room, scheduledAt));
        Future<Seans> future2 = es.submit(() -> schedule(seansScheduler, movie2, room, scheduledAt.plus(movie.getDuration()).plusMinutes(10)));
        Seans seans = null, seans2 = null;
        Throwable thrown = null;
        try {
            seans = future.get();
            seans2 = future2.get();
        } catch (ExecutionException exception) {
            thrown = exception.getCause();
        }

        //then
        assertNotNull(thrown);
        assertEquals(OverlappingEventException.class, thrown.getClass());
        assertTrue(seans != null || future2.get() != null);
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
    public void shouldNotSchedulePremierOutsideDefinedTimeWindow() {
        //given
        Settings settings = new Settings();
        settings.setPremieresRequiredSchedule(new TimeFrame(17, 21));

        RoomEventRepository roomEventRepository = new RoomEventRepositoryMock();
        EventRoomScheduler eventRoomScheduler = new EventRoomScheduler(roomEventRepository);
        SeansScheduler seansScheduler = new SeansScheduler(eventRoomScheduler, settings);
        Movie movie = new Movie("Shrek", 90, false, true);
        Room room = new Room("Room 1", 20);
        ZonedDateTime scheduledAt = ZonedDateTime.of(2011, 3, 10, 16, 59, 59, 999, ZoneId.systemDefault());

        //when
        schedule(seansScheduler, movie, room, scheduledAt);
    }

    @Test(expected = ScheduledOutsideOpeningHoursException.class)
    public void shouldNotScheduleSeansOutsideOpeningHours() {
        //given
        Settings settings = new Settings();
        settings.setOpeningHours(new TimeFrame(8, 22));

        RoomEventRepository roomEventRepository = new RoomEventRepositoryMock();
        EventRoomScheduler eventRoomScheduler = new EventRoomScheduler(roomEventRepository);
        SeansScheduler seansScheduler = new SeansScheduler(eventRoomScheduler, settings);
        Movie movie = new Movie("Shrek", 90, false, true);
        Room room = new Room("Room 1", 20);
        ZonedDateTime scheduledAt = ZonedDateTime.of(2011, 3, 10, 7, 59, 59, 999, ZoneId.systemDefault());

        //when
        schedule(seansScheduler, movie, room, scheduledAt);
    }

    private Seans schedule(SeansScheduler seansScheduler, Movie movie, Room room, ZonedDateTime scheduledAt) {
        Seans seans = new Seans(movie, room, scheduledAt);
        return seansScheduler.scheduleIfAvailable(seans);
    }
}