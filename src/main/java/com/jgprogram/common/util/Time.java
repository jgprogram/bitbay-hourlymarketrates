package com.jgprogram.common.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Time {

    public static Date now() {
        return new Date();
    }

    public static Date currentFullHour() {
        return Date.from(
                LocalDateTime.now()
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());

    }

    public static Date previousFullHour(Date current) {
        return Date.from(
                LocalDateTime.ofInstant(current.toInstant(), ZoneId.systemDefault())
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0)
                        .minusHours(1)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());

    }

    public static Date nextFullHour(Date current) {
        return Date.from(
                LocalDateTime.ofInstant(current.toInstant(), ZoneId.systemDefault())
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0)
                        .plusHours(1)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());

    }

    public static Date oneYearLater(Date since) {
        LocalDateTime nowDate = LocalDateTime.now();
        LocalDateTime sinceDate
                = LocalDateTime.ofInstant(since.toInstant(), ZoneId.systemDefault());

        if (Duration.between(sinceDate, nowDate).compareTo(Duration.ofDays(365)) > 0) {
            return Date.from(sinceDate.plusYears(1)
                    .atZone(ZoneId.systemDefault())
                    .toInstant());
        } else {
            return Date.from(nowDate
                    .atZone(ZoneId.systemDefault())
                    .toInstant());
        }
    }
}
