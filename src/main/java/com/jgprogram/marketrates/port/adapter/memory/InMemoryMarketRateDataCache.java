package com.jgprogram.marketrates.port.adapter.memory;

import com.jgprogram.marketrates.bitbay.MarketRateDataLoaded;
import com.jgprogram.marketrates.cache.MarketRateDataCache;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Profile("inMemoryRepositories")
public class InMemoryMarketRateDataCache implements MarketRateDataCache {

    private final List<MarketRateDataLoaded> rates = new ArrayList<>();

    @Override
    public void add(MarketRateDataLoaded marketRateDataLoaded) {
        rates.add(marketRateDataLoaded);
    }

    @Override
    public List<MarketRateDataLoaded> findByRequestId(String requestId) {
        return rates.stream()
                .filter(r -> r.requestId().equals(requestId))
                .collect(Collectors.toList());
    }

    @Override
    public void removeByRequestId(String requestId) {
        rates.removeIf(r -> r.requestId().equals(requestId));
    }
}
