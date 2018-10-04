package com.jgprogram.bitbay.marketrates.port.adapter.bitbay;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
class TradingTickerService {

    @Async
    public CompletableFuture<List<Market>> getMarkets() {
        throw new UnsupportedOperationException();
    }
}
