package com.jgprogram.marketrates.application.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jgprogram.common.jackson.DayDeserializer;
import com.jgprogram.common.jackson.DaySerializer;

import java.util.Date;
import java.util.List;

public class HourlyMarketRatesDTO {

    @JsonSerialize(using = DaySerializer.class)
    @JsonDeserialize(using = DayDeserializer.class)
    private final Date day;
    private final List<HourRateDTO> hours;

    public HourlyMarketRatesDTO(Date day, List<HourRateDTO> hours) {
        this.day = day;
        this.hours = hours;
    }

    public Date getDay() {
        return day;
    }

    public List<HourRateDTO> getHours() {
        return hours;
    }

    public static class HourRateDTO {
        private final Integer hour;
        private final Double avgRate;

        public HourRateDTO(Integer hour, Double avgRate) {
            this.hour = hour;
            this.avgRate = avgRate;
        }

        public Integer getHour() {
            return hour;
        }

        public Double getAvgRate() {
            return avgRate;
        }
    }
}
