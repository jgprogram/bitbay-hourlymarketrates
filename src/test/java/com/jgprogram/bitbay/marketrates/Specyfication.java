package com.jgprogram.bitbay.marketrates;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Specyfication {

    protected Date currentFullHour() {
        return Date.from(
                LocalDateTime.now()
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());

    }

    protected Date previousFullHour(Date current) {
        return Date.from(
                LocalDateTime.ofInstant(current.toInstant(), ZoneId.systemDefault())
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0)
                        .minusHours(1)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());

    }

    protected Date nextFullHour(Date current) {
        return Date.from(
                LocalDateTime.ofInstant(current.toInstant(), ZoneId.systemDefault())
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0)
                        .plusHours(1)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());

    }
}
