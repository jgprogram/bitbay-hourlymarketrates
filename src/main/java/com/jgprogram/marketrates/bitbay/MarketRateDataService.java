package com.jgprogram.marketrates.bitbay;

import com.jgprogram.marketrates.event.EventBus;
import com.jgprogram.common.util.TimeFullUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class MarketRateDataService {

    private static final Logger logger = LoggerFactory.getLogger(MarketRateDataService.class);

    private final TradingTickerService tradingTickerService;
    private final TradingCandlestickService tradingCandlestickService;

    @Autowired
    public MarketRateDataService(TradingTickerService tradingTickerService,
                                 TradingCandlestickService tradingCandlestickService) {
        this.tradingTickerService = tradingTickerService;
        this.tradingCandlestickService = tradingCandlestickService;
    }

    @Async
    public CompletableFuture<Void> loadDataFromOneYear(Date since) {
        final Date to = TimeFullUnit.oneYearLater(since);
        final String requestId = UUID.randomUUID().toString();
        logger.info("New market rate data request " + requestId + " was created.");

        try {
            List<Market> markets = tradingTickerService.getMarkets().get(60, TimeUnit.SECONDS);

            List<CompletableFuture<List<MarketRate>>> futuresOfMarketRates = loadMarketsRates(markets, since, to);
            for (CompletableFuture<List<MarketRate>> future : futuresOfMarketRates) {
                List<MarketRate> marketRates = future.get();
                marketRates.forEach(mr -> publishLoadedEvent(requestId, mr));
            }

            EventBus.instance()
                    .publish(new MarketRateDataLoadCompleted(requestId, since, to));
            logger.info(requestId + " - completed.");
        } catch (Exception e) {
            EventBus.instance()
                    .publish(new MarketRateDataLoadErrorOccurred(requestId));
            logger.error(requestId + " - canceled.", e);
        } finally {
            return CompletableFuture.completedFuture(null);
        }
    }

    private List<CompletableFuture<List<MarketRate>>> loadMarketsRates(List<Market> markets, Date since, Date to) throws Exception {
        List<CompletableFuture<List<MarketRate>>> futures = new ArrayList<>(markets.size());
        for (Market m : markets) {
            futures.add(tradingCandlestickService.getHourlyMarketRatesSince(m.getCode(), since, to));
        }

        return futures;
    }

    private void publishLoadedEvent(final String requestId, MarketRate marketRates) {
        MarketRateDataLoaded event = new MarketRateDataLoaded(
                requestId,
                marketRates.getCode(),
                marketRates.getDate(),
                marketRates.getO(),
                marketRates.getC(),
                marketRates.getH(),
                marketRates.getV()
        );

        EventBus.instance()
                .publish(event);
    }
}
