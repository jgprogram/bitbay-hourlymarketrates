package com.jgprogram.marketrates.port.adapter.memory;

import com.jgprogram.marketrates.bitbay.MarketRateDataLoaded;
import com.jgprogram.marketrates.cache.MarketRateDataCache;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InMemoryMarketRateDataCache implements MarketRateDataCache {

    @Override
    public void add(MarketRateDataLoaded marketRateDataLoaded) {

    }

    @Override
    public List<MarketRateDataLoaded> findByRequestId(String requestId) {
        return null;
    }

    @Override
    public void removeByRequestId(String requestId) {

    }
}
