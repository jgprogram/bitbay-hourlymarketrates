package com.jgprogram.marketrates.domain.model;

import java.util.Date;

public interface MarketRateRepository {
    void add(MarketRate marketRate);

    Date findLatestDate();

    MarketRate findByMarketCodeAndDate(String marketCode, Date date);
}
