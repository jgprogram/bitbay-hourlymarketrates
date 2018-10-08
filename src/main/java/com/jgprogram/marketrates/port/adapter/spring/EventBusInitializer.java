package com.jgprogram.marketrates.port.adapter.spring;

import com.jgprogram.marketrates.cache.MarketRateDataLoadCompletedSubscriber;
import com.jgprogram.marketrates.cache.MarketRateDataLoadErrorOccurredSubscriber;
import com.jgprogram.marketrates.cache.MarketRateDataLoadedSubscriber;
import com.jgprogram.common.event.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventBusInitializer {

    @Autowired
    MarketRateDataLoadCompletedSubscriber marketRateDataLoadCompletedSubscriber;

    @Autowired
    MarketRateDataLoadedSubscriber marketRateDataLoadedSubscriber;

    @Autowired
    MarketRateDataLoadErrorOccurredSubscriber marketRateDataLoadErrorOccurredSubscriber;

    @EventListener({ApplicationReadyEvent.class})
    public void onAppReady() {
        EventBus.instance()
                .subscribe(marketRateDataLoadedSubscriber);
        EventBus.instance()
                .subscribe(marketRateDataLoadCompletedSubscriber);
        EventBus.instance()
                .subscribe(marketRateDataLoadErrorOccurredSubscriber);
    }
}
