package com.highway.cinema.domain;

import com.highway.cinema.domain.dao.RoomEventRepository;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

public class SeansSchedulingService {
    private RoomEventRepository roomEventRepository;
    private Settings settings;

    public SeansSchedulingService(RoomEventRepository roomEventRepository, Settings settings) {
        this.roomEventRepository = roomEventRepository;
        this.settings = settings;
    }

    public synchronized Seans scheduleSeans(Movie movie, Room room, ZonedDateTime startAt) {
        Seans seans = new Seans(movie, room, startAt);
        ZonedDateTime seansEnd = startAt.plus(movie.getDuration());
        ScheduledMaintenance maintenance = new ScheduledMaintenance(room, seansEnd);

        if (overlapsWithOtherEvents(seans) || overlapsWithOtherEvents(maintenance)) {
            throw new OverlappingEventException();
        }
        if (!scheduledWithinOpeningHours(seans)) {
            throw new ScheduledOutsideOpeningHoursException();
        }
        if (seans.getMovie().isPremier() && !premierScheduledWithinRequiredHours(seans)) {
            throw new ScheduledOutsideHoursRequiredForPremierException();
        }

        roomEventRepository.save(seans);
        roomEventRepository.save(maintenance);
        return seans;
    }

    private boolean scheduledWithinOpeningHours(Seans seans) {
        return scheduledWithinTimeWindow(seans, settings.getOpeningHours());
    }

    private boolean premierScheduledWithinRequiredHours(Seans seans) {
        return scheduledWithinTimeWindow(seans, settings.getPremieresRequiredSchedule());
    }

    private boolean scheduledWithinTimeWindow(Seans seans, TimeFrame timeFrame) {
        return timeFrame == null
                || (seans.getStart().getHour() >= timeFrame.getStartHour()
                && seans.getEnd().getHour() < timeFrame.getEndHour());
    }

    private boolean overlapsWithOtherEvents(RoomEvent roomEvent) {
        return roomEventRepository
                .inSameRoomAndOverlapsWithAnyWithinTimeRange(roomEvent.getRoom(), roomEvent.getStart(), roomEvent.getEnd());
    }

//    private List<TimeFrame> getAvailableSlots(Room room, LocalDate localDate) {
//        return roomEventRepository
//                .inSameRoomAndOverlapsWithAnyWithinTimeRange(roomEvent.getRoom(), roomEvent.getStart(), roomEvent.getEnd());
//        return List.of();
//    }

    public static final class OverlappingEventException extends RuntimeException {
    }

    public static final class ScheduledOutsideOpeningHoursException extends RuntimeException {
    }

    public static final class ScheduledOutsideHoursRequiredForPremierException extends RuntimeException {
    }
}
