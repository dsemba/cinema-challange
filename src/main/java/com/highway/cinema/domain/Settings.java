package com.highway.cinema.domain;

import com.highway.cinema.domain.TimeFrame;
import lombok.Data;

@Data
public class Settings {
    private TimeFrame openingHours;
    private TimeFrame premieresRequiredSchedule;
}
