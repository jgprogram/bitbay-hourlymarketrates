package com.jgprogram.bitbay.marketrates.port.adapter.bitbay;

import com.jgprogram.bitbay.marketrates.event.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class MarketRateDataAdapter {

    private static final Logger logger = LoggerFactory.getLogger(MarketRateDataAdapter.class);

    private final TradingTickerService tradingTickerService;
    private final TradingCandlestickService tradingCandlestickService;

    @Autowired
    public MarketRateDataAdapter(TradingTickerService tradingTickerService, TradingCandlestickService tradingCandlestickService) {
        this.tradingTickerService = tradingTickerService;
        this.tradingCandlestickService = tradingCandlestickService;
    }

    @Async
    public CompletableFuture<Void> loadDataSince(Date since) {
        final String requestId = UUID.randomUUID().toString();
        logger.info("New request " + requestId + " was created.");

        try {
            List<Market> markets = tradingTickerService.getMarkets().get(60, TimeUnit.SECONDS);
            logger.info("Loaded " + markets.size() + " markets.");

            //TODO CREATE TASKS
            for(Market market : markets) {
                tradingCandlestickService.getHourlyMarketRatesSince(market.getCode(), since);
            }
        } catch (Exception e) {
            EventBus.instance()
                    .publish(new MarketRateDataLoadErrorOccurred(requestId));
            logger.error("Request " + requestId + " was canceled." , e);
        } finally {
            return CompletableFuture.completedFuture(null);
        }
    }
}
