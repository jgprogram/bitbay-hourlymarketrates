package com.jgprogram.marketrates.cache;

import com.jgprogram.marketrates.event.EventSubscriber;
import com.jgprogram.marketrates.bitbay.MarketRateDataLoaded;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MarketRateDataLoadedSubscriber implements EventSubscriber<MarketRateDataLoaded> {

    private final MarketRateDataCache marketRateDataCache;

    @Autowired
    public MarketRateDataLoadedSubscriber(MarketRateDataCache marketRateDataCache) {
        this.marketRateDataCache = marketRateDataCache;
    }

    @Override
    public void handleEvent(MarketRateDataLoaded marketRateDataLoaded) {
        marketRateDataCache.add(marketRateDataLoaded);
    }

    @Override
    public Class<MarketRateDataLoaded> subscribedToEventType() {
        return MarketRateDataLoaded.class;
    }
}
