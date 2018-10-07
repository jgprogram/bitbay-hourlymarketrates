package com.jgprogram.bitbay.marketrates.cache;

import com.jgprogram.bitbay.marketrates.application.MarketRateService;
import com.jgprogram.bitbay.marketrates.application.dto.MarketRateDTO;
import com.jgprogram.bitbay.marketrates.event.EventSubscriber;
import com.jgprogram.bitbay.marketrates.port.adapter.bitbay.MarketRateDataLoadCompleted;
import com.jgprogram.bitbay.marketrates.port.adapter.bitbay.MarketRateDataLoaded;
import com.jgprogram.bitbay.marketrates.cache.MarketRateDataCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MarketRateDataLoadCompletedSubscriber implements EventSubscriber<MarketRateDataLoadCompleted> {

    private final MarketRateDataCache marketRateDataCache;
    private final MarketRateService marketRateService;

    @Autowired
    public MarketRateDataLoadCompletedSubscriber(MarketRateDataCache marketRateDataCache, MarketRateService marketRateService) {
        this.marketRateDataCache = marketRateDataCache;
        this.marketRateService = marketRateService;
    }

    @Override
    public void handleEvent(MarketRateDataLoadCompleted event) {
        marketRateDataCache
                .findByRequestId(event.requestId()).stream()
                .map(this::mapToMarketRateDTO)
                .forEach(marketRateService::createMarketRate);
    }

    private MarketRateDTO mapToMarketRateDTO(MarketRateDataLoaded source) {
        return new MarketRateDTO(
                source.code(),
                source.date(),
                source.open(),
                source.close(),
                source.highest(),
                source.lowest()
        );
    }

    @Override
    public Class<MarketRateDataLoadCompleted> subscribedToEventType() {
        return MarketRateDataLoadCompleted.class;
    }
}
