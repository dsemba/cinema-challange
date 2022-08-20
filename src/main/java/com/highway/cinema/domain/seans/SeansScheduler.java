package com.highway.cinema.domain.seans;

import com.highway.cinema.domain.RoomEvent;
import com.highway.cinema.domain.TimeFrame;
import com.highway.cinema.domain.event.EventRoomScheduler;
import com.highway.cinema.domain.seans.exception.ScheduledOutsideHoursRequiredForPremierException;
import com.highway.cinema.domain.seans.exception.ScheduledOutsideOpeningHoursException;

import java.lang.reflect.InvocationTargetException;

public class SeansScheduler {
    private final EventRoomScheduler eventRoomScheduler;
    private final WithinHoursRestriction openingHoursRestriction;
    private final WithinHoursRestriction premierHoursRestriction;

    public SeansScheduler(EventRoomScheduler eventRoomScheduler, Settings settings) {
        this.eventRoomScheduler = eventRoomScheduler;
        this.openingHoursRestriction = settings.getOpeningHours() != null
                ? new WithinHoursRestriction(settings.getOpeningHours(), ScheduledOutsideOpeningHoursException.class)
                : null;
        this.premierHoursRestriction = settings.getPremieresRequiredSchedule() != null
                ? new WithinHoursRestriction(settings.getPremieresRequiredSchedule(),
                    ScheduledOutsideHoursRequiredForPremierException.class)
                : null;
    }

    public Seans scheduleIfAvailable(Seans seans) {
        validateWithinHours(seans.getScreening());
        validateWithinHours(seans.getMaintenance());
        eventRoomScheduler.validateAndSchedule(seans.getEvents());
        return seans;
    }

    private void validateWithinHours(RoomEvent roomEvent) {
        validateWithinHoursRestriction(roomEvent, openingHoursRestriction);
        if (roomEvent instanceof MovieScreening) {
            if (((MovieScreening) roomEvent).getMovie().isPremier()) {
                validateWithinHoursRestriction(roomEvent, premierHoursRestriction);
            }
        }
    }

    private void validateWithinHoursRestriction(RoomEvent event, WithinHoursRestriction restriction) {
        if (restriction == null) {
            return;
        }
        if (!scheduledWithin(event, restriction.getTimeFrame())) {
            try {
                throw restriction.getExceptionClass().getDeclaredConstructor().newInstance();
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean scheduledWithin(RoomEvent event, TimeFrame timeFrame) {
        return timeFrame == null
                || (event.getStart().getHour() >= timeFrame.getStartHour()
                && event.getEnd().getHour() < timeFrame.getEndHour());
    }
}
