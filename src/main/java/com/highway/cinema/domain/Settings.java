package com.highway.cinema.domain;

import lombok.Data;

@Data
public class Settings {
    private TimeFrame openingHours;
    private TimeFrame premieresRequiredSchedule;
}
