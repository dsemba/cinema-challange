package com.highway.cinema.domain;

import com.highway.cinema.domain.dao.RoomEventRepository;
import com.highway.cinema.domain.exception.OverlappingEventException;
import com.highway.cinema.domain.exception.ScheduledOutsideHoursRequiredForPremierException;
import com.highway.cinema.domain.exception.ScheduledOutsideOpeningHoursException;
import com.highway.cinema.domain.seans.MovieScreening;
import com.highway.cinema.domain.seans.ScheduledMaintenance;
import com.highway.cinema.domain.seans.Seans;

import java.time.ZonedDateTime;

public class SeansSchedulingService {
    private RoomEventRepository roomEventRepository;
    private Settings settings;

    public SeansSchedulingService(RoomEventRepository roomEventRepository, Settings settings) {
        this.roomEventRepository = roomEventRepository;
        this.settings = settings;
    }

    public synchronized Seans scheduleSeans(Movie movie, Room room, ZonedDateTime startAt) {
        Seans seans = new Seans(movie, room, startAt);
        MovieScreening screening = seans.getScreening();
        ScheduledMaintenance maintenance = seans.getMaintenance();

        if (overlapsWithOtherEvents(screening) || overlapsWithOtherEvents(maintenance)) {
            throw new OverlappingEventException();
        }
        if (!scheduledWithinOpeningHours(screening)) {
            throw new ScheduledOutsideOpeningHoursException();
        }
        if (screening.getMovie().isPremier() && !premierScheduledWithinRequiredHours(screening)) {
            throw new ScheduledOutsideHoursRequiredForPremierException();
        }

        roomEventRepository.save(screening);
        roomEventRepository.save(maintenance);
        return seans;
    }

    private boolean scheduledWithinOpeningHours(MovieScreening screening) {
        return scheduledWithinTimeWindow(screening, settings.getOpeningHours());
    }

    private boolean premierScheduledWithinRequiredHours(MovieScreening screening) {
        return scheduledWithinTimeWindow(screening, settings.getPremieresRequiredSchedule());
    }

    private boolean scheduledWithinTimeWindow(MovieScreening screening, TimeFrame timeFrame) {
        return timeFrame == null
                || (screening.getStart().getHour() >= timeFrame.getStartHour()
                && screening.getEnd().getHour() < timeFrame.getEndHour());
    }

    private boolean overlapsWithOtherEvents(RoomEvent roomEvent) {
        return roomEventRepository
                .inSameRoomAndOverlapsWithAnyWithinTimeRange(roomEvent.getRoom(), roomEvent.getStart(), roomEvent.getEnd());
    }
}
