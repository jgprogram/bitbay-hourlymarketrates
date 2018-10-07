package com.jgprogram.marketrates.port.adapter.memory;

import com.jgprogram.marketrates.domain.model.MarketRate;
import com.jgprogram.marketrates.domain.model.MarketRateRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class InMemoryMarketRateRepository implements MarketRateRepository {
    @Override
    public void add(MarketRate marketRate) {

    }

    @Override
    public Date findLatestDate() {
        return null;
    }
}
