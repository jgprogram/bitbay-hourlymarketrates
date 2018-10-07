package com.jgprogram.bitbay.marketrates.cache;

import com.jgprogram.bitbay.marketrates.port.adapter.bitbay.MarketRateDataLoaded;

import java.util.List;

public interface MarketRateDataCache {

    void add(MarketRateDataLoaded marketRateDataLoaded);

    List<MarketRateDataLoaded> findByRequestId(String requestId);

    void removeByRequestId(String requestId);
}
