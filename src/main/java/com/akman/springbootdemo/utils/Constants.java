package com.akman.springbootdemo.utils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class Constants {
    public static LocalDateTime weekDayDateTimeNow() {
        LocalDateTime now = LocalDateTime.now();
        return DayOfWeek.SATURDAY.equals(now.getDayOfWeek()) ? now.toLocalDate().plusDays(2).atStartOfDay()
                : DayOfWeek.SUNDAY.equals(now.getDayOfWeek()) ? now.toLocalDate().plusDays(1).atStartOfDay() : now;
    }
}