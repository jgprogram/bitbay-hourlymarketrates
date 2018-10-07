package com.jgprogram.marketrates.bitbay;

import com.jgprogram.marketrates.event.Event;

import java.util.Date;

public class MarketRateDataLoaded implements Event {

    private final String requestId;
    private final Date occurredOn;
    private final String code;
    private final Date date;
    private final Double open;
    private final Double close;
    private final Double highest;
    private final Double lowest;

    public MarketRateDataLoaded(String requestId, String code, Date date, Double open, Double close, Double highest, Double lowest) {
        this.requestId = requestId;
        this.occurredOn = new Date();
        this.code = code;
        this.date = date;
        this.open = open;
        this.close = close;
        this.highest = highest;
        this.lowest = lowest;
    }

    public String requestId() {
        return requestId;
    }

    @Override
    public Date occurredOn() {
        return occurredOn;
    }

    public String code() {
        return code;
    }

    public Date date() {
        return date;
    }

    public Double open() {
        return open;
    }

    public Double close() {
        return close;
    }

    public Double highest() {
        return highest;
    }

    public Double lowest() {
        return lowest;
    }
}
