package com.jgprogram.marketrates.bitbay;

import com.jgprogram.marketrates.event.Event;

import java.util.Date;

public class MarketRateDataLoadErrorOccurred implements Event {

    private final Date occurredOn;
    private final String requestId;

    public MarketRateDataLoadErrorOccurred(String requestId) {
        this.occurredOn = new Date();
        this.requestId = requestId;
    }

    @Override
    public Date occurredOn() {
        return occurredOn;
    }

    public String requestId() {
        return requestId;
    }
}
