package com.highway.cinema.domain.seans;

import com.highway.cinema.domain.Movie;
import com.highway.cinema.domain.Room;
import org.junit.Test;

import java.time.ZonedDateTime;

import static org.junit.Assert.*;

public class SeansTest {

    @Test
    public void shouldScheduleMaintenanceAfterScreening() {
        //given
        Movie movie = new Movie("Shrek", 90);
        Room room = new Room("Room 1", 20);
        ZonedDateTime scheduledAt = ZonedDateTime.now();
        //when
        Seans seans = new Seans(movie, room, scheduledAt);
        //then
        ScheduledMaintenance maintenance = seans.getMaintenance();
        assertEquals(scheduledAt.plus(movie.getDuration()), maintenance.getStart());
        assertEquals(scheduledAt.plus(movie.getDuration())
                .plusMinutes(room.getCleaningDurationInMinutes()), maintenance.getEnd());
    }
}