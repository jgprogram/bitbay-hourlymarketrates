package com.jgprogram.marketrates.cache;

import com.jgprogram.common.event.EventSubscriber;
import com.jgprogram.marketrates.bitbay.MarketRateDataLoadErrorOccurred;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MarketRateDataLoadErrorOccurredSubscriber implements EventSubscriber<MarketRateDataLoadErrorOccurred> {

    private final MarketRateDataCache marketRateDataCache;

    @Autowired
    public MarketRateDataLoadErrorOccurredSubscriber(MarketRateDataCache marketRateDataCache) {
        this.marketRateDataCache = marketRateDataCache;
    }

    @Override
    public void handleEvent(MarketRateDataLoadErrorOccurred event) {
            marketRateDataCache.removeByRequestId(event.requestId());
    }

    @Override
    public Class<MarketRateDataLoadErrorOccurred> subscribedToEventType() {
        return MarketRateDataLoadErrorOccurred.class;
    }
}
