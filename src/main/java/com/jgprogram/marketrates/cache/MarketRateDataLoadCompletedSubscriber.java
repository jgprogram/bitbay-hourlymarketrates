package com.jgprogram.marketrates.cache;

import com.jgprogram.marketrates.application.MarketRateService;
import com.jgprogram.marketrates.application.dto.MarketRateDTO;
import com.jgprogram.common.event.EventSubscriber;
import com.jgprogram.marketrates.bitbay.MarketRateDataLoadCompleted;
import com.jgprogram.marketrates.bitbay.MarketRateDataLoaded;
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
