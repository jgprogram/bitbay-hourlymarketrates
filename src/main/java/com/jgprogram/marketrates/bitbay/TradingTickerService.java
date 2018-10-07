package com.jgprogram.marketrates.bitbay;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TradingTickerService {

    CompletableFuture<List<MarketData>> getMarkets() throws Exception;
}
