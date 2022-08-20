package com.highway.cinema.domain.seans;

import com.highway.cinema.domain.Movie;
import com.highway.cinema.domain.Room;
import com.highway.cinema.domain.RoomEvent;
import com.highway.cinema.domain.RoomEventType;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SeansTest {

    @Test
    public void shouldReserveScreeningAndMaintenanceEvents() {
        //given
        Movie movie = new Movie("Shrek", 90);
        Room room = new Room("Room 1", 20);
        ZonedDateTime scheduledAt = ZonedDateTime.now();
        //when
        Seans seans = new Seans(movie, room, scheduledAt);
        //then
        List<RoomEvent> roomEventList = seans.getEvents();
        assertEquals(2, roomEventList.size());
        Optional<RoomEvent> screening = roomEventList.stream().filter(x -> x.getType().equals(RoomEventType.SCREENING)).findFirst();
        Optional<RoomEvent> maintenance = roomEventList.stream().filter(x -> x.getType().equals(RoomEventType.MAINTENANCE)).findFirst();
        assertTrue(screening.isPresent());
        assertTrue(maintenance.isPresent());
        assertEquals(scheduledAt, screening.get().getStart());
        assertEquals(scheduledAt.plus(movie.getDuration()), screening.get().getEnd());
        assertEquals(screening.get().getEnd(), maintenance.get().getStart());
        assertEquals(scheduledAt.plus(movie.getDuration())
                .plusMinutes(room.getCleaningDurationInMinutes()), maintenance.get().getEnd());
        assertEquals(room, screening.get().getRoom());
        assertEquals(room, maintenance.get().getRoom());
    }
}