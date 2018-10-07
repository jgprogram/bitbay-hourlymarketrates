package com.jgprogram.marketrates.bitbay;

import com.jgprogram.marketrates.Specification;
import com.jgprogram.marketrates.application.MarketRateService;
import com.jgprogram.marketrates.event.EventBus;
import com.jgprogram.marketrates.cache.MarketRateDataCache;
import com.jgprogram.marketrates.cache.MarketRateDataLoadCompletedSubscriber;
import com.jgprogram.marketrates.cache.MarketRateDataLoadErrorOccurredSubscriber;
import com.jgprogram.marketrates.cache.MarketRateDataLoadedSubscriber;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jgprogram.common.util.TimeFullUnit.*;
import static org.mockito.Mockito.*;

public class MarketRateDataServiceSpec extends Specification {

    @Test
    public void should_load_market_rates_from_bitbay_since_last_hour_and_publish_events_loaded_and_completed() throws Exception {
        final Date now = currentHour();
        final Date previousHour = previousHour(now);
        TradingTickerService tradingTickerService = mockTradingTickerService();
        TradingCandlestickService tradingCandlestickService = mockTradingCandlestickService(previousHour, now);
        MarketRateDataService adapter = new MarketRateDataService(tradingTickerService, tradingCandlestickService);
        MarketRateDataLoadedSubscriber dataLoadedSubscriber = marketRateDataLoadedSubscriber();
        MarketRateDataLoadCompletedSubscriber dataLoadCompletedSubscriber = marketRateDataLoadCompletedSubscriber();

        adapter.loadDataFromOneYear(previousHour);

        verify(dataLoadedSubscriber, times(4))
                .handleEvent(any(MarketRateDataLoaded.class));
        verify(dataLoadCompletedSubscriber, times(1))
                .handleEvent(any(MarketRateDataLoadCompleted.class));
    }

    @Test
    public void when_error_occurs_should_receive_notification_about_that() throws Exception {
        final Date now = currentHour();
        final Date previousHour = previousHour(now);
        TradingTickerService tradingTickerService = mockTradingTickerService();
        TradingCandlestickService tradingCandlestickService = mockTradingCandlestickServiceWithException(previousHour, now);
        MarketRateDataService adapter = new MarketRateDataService(tradingTickerService, tradingCandlestickService);
        MarketRateDataLoadErrorOccurredSubscriber errorSubscriber = marketRateDataLoadErrorOccurredSubscriber();

        adapter.loadDataFromOneYear(previousHour);

        verify(errorSubscriber, times(1))
                .handleEvent(any(MarketRateDataLoadErrorOccurred.class));
    }

    private MarketRateDataLoadErrorOccurredSubscriber marketRateDataLoadErrorOccurredSubscriber() {
        MarketRateDataCache cache = mock(MarketRateDataCache.class);
        MarketRateDataLoadErrorOccurredSubscriber s = spy(new MarketRateDataLoadErrorOccurredSubscriber(cache));
        EventBus.instance().subscribe(s);
        return s;
    }

    private MarketRateDataLoadCompletedSubscriber marketRateDataLoadCompletedSubscriber() {
        MarketRateDataCache cache = mock(MarketRateDataCache.class);
        MarketRateService service = mock(MarketRateService.class);
        MarketRateDataLoadCompletedSubscriber s = spy(new MarketRateDataLoadCompletedSubscriber(cache, service));
        EventBus.instance().subscribe(s);
        return s;
    }

    private MarketRateDataLoadedSubscriber marketRateDataLoadedSubscriber() {
        MarketRateDataCache cache = mock(MarketRateDataCache.class);
        MarketRateDataLoadedSubscriber s = spy(new MarketRateDataLoadedSubscriber(cache));
        EventBus.instance().subscribe(s);
        return s;
    }

    private TradingCandlestickService mockTradingCandlestickService(Date since, Date to) throws Exception {
        TradingCandlestickService tradingCandlestickService = mock(TradingCandlestickService.class);
        when(tradingCandlestickService.getHourlyMarketRatesSince("BTC-USD", since, to))
                .thenReturn(CompletableFuture.completedFuture(sampleMarketRates("BTC-USD", since)));
        when(tradingCandlestickService.getHourlyMarketRatesSince("ZRX-EUR", since, to))
                .thenReturn(CompletableFuture.completedFuture(sampleMarketRates("ZRX-EUR", since)));
        return tradingCandlestickService;
    }

    private TradingCandlestickService mockTradingCandlestickServiceWithException(Date since, Date to) throws Exception {
        TradingCandlestickService tradingCandlestickService = mock(TradingCandlestickService.class);
        when(tradingCandlestickService.getHourlyMarketRatesSince("BTC-USD", since, to))
                .thenReturn(CompletableFuture.completedFuture(sampleMarketRates("BTC-USD", since)));
        when(tradingCandlestickService.getHourlyMarketRatesSince("ZRX-EUR", since, to))
                .thenThrow(Exception.class);
        return tradingCandlestickService;
    }

    private TradingTickerService mockTradingTickerService() throws Exception {
        final CompletableFuture<List<Market>> markets = CompletableFuture.completedFuture(sampleMarkets());
        TradingTickerService tradingTickerService = mock(TradingTickerService.class);
        when(tradingTickerService.getMarkets())
                .thenReturn(markets);
        return tradingTickerService;
    }

    private List<MarketRate> sampleMarketRates(String marketCode, Date since) {
        final long limit = Duration.between(LocalDateTime.ofInstant(since.toInstant(), ZoneId.systemDefault()),
                LocalDateTime.now())
                .toHours() + 1;

        return Stream.iterate(
                sampleMarketRate(marketCode, since),
                mr -> sampleMarketRate(marketCode, nextHour(mr.getDate())))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private MarketRate sampleMarketRate(String marketCode, Date date) {
        return new MarketRate(
                marketCode,
                date,
                1545D,
                1555D,
                1590D,
                14741D,
                0D);
    }

    private List<Market> sampleMarkets() {
        return Arrays.asList(
                new Market("BTC-USD"),
                new Market("ZRX-EUR"));
    }
}
