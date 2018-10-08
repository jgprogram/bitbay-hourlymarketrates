package com.jgprogram.common.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TimeFullUnit {
    public static Date toFullHour(Date date) {
        return Date.from(
                LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public static Date currentHour() {
        return Date.from(
                LocalDateTime.now()
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());

    }

    public static Date previousHour(Date current) {
        return Date.from(
                LocalDateTime.ofInstant(current.toInstant(), ZoneId.systemDefault())
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0)
                        .minusHours(1)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());

    }

    public static Date nextHour(Date current) {
        return Date.from(
                LocalDateTime.ofInstant(current.toInstant(), ZoneId.systemDefault())
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0)
                        .plusHours(1)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());

    }

    public static Date nextMinute(Date current) {
        return Date.from(
                LocalDateTime.ofInstant(current.toInstant(), ZoneId.systemDefault())
                        .withSecond(0)
                        .withNano(0)
                        .plusMinutes(1)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public static Date oneYearLater(Date since) {
        LocalDateTime nowDate = LocalDateTime.now()
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        LocalDateTime sinceDate
                = LocalDateTime.ofInstant(toFullHour(since).toInstant(), ZoneId.systemDefault());

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

    public static long millisToNextMinute() {
        LocalDateTime nowDate = LocalDateTime.now();
        LocalDateTime nextFullMinute = nowDate
                .withSecond(0)
                .withNano(0)
                .plusMinutes(1);

        return Duration.between(nowDate, nextFullMinute).get(ChronoUnit.MILLIS);
    }

    public static Date dayFrom(Date date) {
        return Date.from(
                LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
                        .withHour(0)
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public static Integer hourFrom(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
                .getHour();
    }
}
