package com.jgprogram.bitbay.marketrates.port.adapter.bitbay;

import com.jgprogram.bitbay.marketrates.event.Event;

import java.util.Date;

public class MarketRateDataLoadCompleted implements Event {

    private final String requestId;
    private final Date occurredOn;
    private final Date dataSince;

    public MarketRateDataLoadCompleted(String requestId, Date since) {
        this.requestId = requestId;
        this.occurredOn = new Date();
        this.dataSince = since;
    }

    public String requestId() {
        return requestId;
    }

    @Override
    public Date occurredOn() {
        return occurredOn;
    }

    public Date dataSince() {
        return dataSince;
    }
}
