package com.jgprogram.marketrates.application.dto;

import java.util.Date;

public class MarketRateDTO {
    private final String marketCode;
    private final Date date;
    private final Double open;
    private final Double close;
    private final Double highest;
    private final Double lowest;

    public MarketRateDTO(String marketCode, Date date, Double open, Double close, Double highest, Double lowest) {
        this.marketCode = marketCode;
        this.date = date;
        this.open = open;
        this.close = close;
        this.highest = highest;
        this.lowest = lowest;
    }

    public String getMarketCode() {
        return marketCode;
    }

    public Date getDate() {
        return date;
    }

    public Double getOpen() {
        return open;
    }

    public Double getClose() {
        return close;
    }

    public Double getHighest() {
        return highest;
    }

    public Double getLowest() {
        return lowest;
    }
}
