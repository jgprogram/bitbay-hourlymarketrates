package com.jgprogram.marketrates.bitbay;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TradingCandlestickService {

    CompletableFuture<List<MarketRate>> getHourlyMarketRatesSince(String marketCode, Date since, Date to) throws Exception;
}
