package com.jgprogram.bitbay.marketrates.port.adapter.bitbay;

import com.jgprogram.bitbay.marketrates.Specification;
import com.jgprogram.bitbay.marketrates.application.MarketRateService;
import com.jgprogram.bitbay.marketrates.event.EventBus;
import com.jgprogram.bitbay.marketrates.port.adapter.cache.MarketRateDataCache;
import com.jgprogram.bitbay.marketrates.port.adapter.cache.MarketRateDataLoadCompletedSubscriber;
import com.jgprogram.bitbay.marketrates.port.adapter.cache.MarketRateDataLoadErrorOccurredSubscriber;
import com.jgprogram.bitbay.marketrates.port.adapter.cache.MarketRateDataLoadedSubscriber;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jgprogram.common.util.TimeFullUnit.*;
import static org.mockito.Mockito.*;

public class MarketRateDataAdapterSpec extends Specification {

    @Test
    public void should_load_market_rates_from_bitbay_since_last_hour_and_publish_events_loaded_and_completed() throws Exception {
        final Date previousHour = previousHour(currentHour());
        TradingTickerService tradingTickerService = mockTradingTickerService();
        TradingCandlestickService tradingCandlestickService = mockTradingCandlestickService(previousHour);
        MarketRateDataAdapter adapter = new MarketRateDataAdapter(tradingTickerService, tradingCandlestickService);
        MarketRateDataLoadedSubscriber dataLoadedSubscriber = marketRateDataLoadedSubscriber();
        MarketRateDataLoadCompletedSubscriber dataLoadCompletedSubscriber = marketRateDataLoadCompletedSubscriber();

        adapter.loadDataSince(previousHour);

        verify(dataLoadedSubscriber, times(4))
                .handleEvent(any(MarketRateDataLoaded.class));
        verify(dataLoadCompletedSubscriber, times(1))
                .handleEvent(any(MarketRateDataLoadCompleted.class));
    }

    @Test
    public void when_error_occurs_should_receive_notification_about_that() throws Exception {
        final Date previousHour = previousHour(currentHour());
        TradingTickerService tradingTickerService = mockTradingTickerService();
        TradingCandlestickService tradingCandlestickService = mockTradingCandlestickServiceWithException(previousHour);
        MarketRateDataAdapter adapter = new MarketRateDataAdapter(tradingTickerService, tradingCandlestickService);
        MarketRateDataLoadErrorOccurredSubscriber errorSubscriber = marketRateDataLoadErrorOccurredSubscriber();

        adapter.loadDataSince(previousHour);

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

    private TradingCandlestickService mockTradingCandlestickService(Date since) throws Exception {
        TradingCandlestickService tradingCandlestickService = mock(TradingCandlestickService.class);
        when(tradingCandlestickService.getHourlyMarketRatesSince("BTC-USD", since))
                .thenReturn(CompletableFuture.completedFuture(sampleMarketRates("BTC-USD", since)));
        when(tradingCandlestickService.getHourlyMarketRatesSince("ZRX-EUR", since))
                .thenReturn(CompletableFuture.completedFuture(sampleMarketRates("ZRX-EUR", since)));
        return tradingCandlestickService;
    }

    private TradingCandlestickService mockTradingCandlestickServiceWithException(Date since) throws Exception {
        TradingCandlestickService tradingCandlestickService = mock(TradingCandlestickService.class);
        when(tradingCandlestickService.getHourlyMarketRatesSince("BTC-USD", since))
                .thenReturn(CompletableFuture.completedFuture(sampleMarketRates("BTC-USD", since)));
        when(tradingCandlestickService.getHourlyMarketRatesSince("ZRX-EUR", since))
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
