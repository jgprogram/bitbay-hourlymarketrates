package com.jgprogram.marketrates.bitbay;

import com.jgprogram.common.event.Event;

import java.util.Date;

public class MarketRateDataLoadCompleted implements Event {

    private final String requestId;
    private final Date occurredOn;
    private final Date dataSince;
    private final Date dataTo;

    public MarketRateDataLoadCompleted(String requestId, Date since, Date to) {
        this.requestId = requestId;
        this.occurredOn = new Date();
        this.dataSince = since;
        this.dataTo = to;
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

    public Date dataTo() {
        return dataTo;
    }
}
