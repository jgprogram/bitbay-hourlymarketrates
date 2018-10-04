package com.jgprogram.bitbay.marketrates.port.adapter.bitbay;

import org.springframework.scheduling.annotation.Async;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

class TradingCandlestickService {

    @Async
    public CompletableFuture<List<MarketRate>> getHourlyMarketRatesSince(String marketCode, Date since) {
        throw new UnsupportedOperationException();
    }
}
