package com.jgprogram.bitbay.marketrates.port.adapter.memory;

import com.jgprogram.bitbay.marketrates.port.adapter.bitbay.MarketRateDataLoaded;
import com.jgprogram.bitbay.marketrates.cache.MarketRateDataCache;
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
