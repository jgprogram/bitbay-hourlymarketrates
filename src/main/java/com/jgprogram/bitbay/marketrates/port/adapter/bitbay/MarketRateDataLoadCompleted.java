package com.jgprogram.bitbay.marketrates.port.adapter.bitbay;

import com.jgprogram.bitbay.marketrates.event.Event;

import java.util.Date;

public class MarketRateDataLoadCompleted implements Event {

    private final String requestId;
    private final Date occurredOn;

    public MarketRateDataLoadCompleted(String requestId) {
        this.requestId = requestId;
        this.occurredOn = new Date();
    }

    public String requestId() {
        return requestId;
    }

    @Override
    public Date occurredOn() {
        return occurredOn;
    }
}
