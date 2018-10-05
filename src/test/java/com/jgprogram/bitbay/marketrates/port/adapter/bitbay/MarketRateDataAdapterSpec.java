package com.jgprogram.bitbay.marketrates.port.adapter.bitbay;

import com.jgprogram.bitbay.marketrates.Specification;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MarketRateDataAdapterSpec extends Specification {

    private final Date now = new Date();

    @Test
    @Ignore
    public void should_get_BitBay_markets() throws ExecutionException, InterruptedException {
//        TradingTickerService tradingTickerService = mock(TradingTickerService.class);
//        TradingCandlestickService tradingCandlestickService = mock(TradingCandlestickService.class);
//        MarketRateDataAdapter marketRateDataAdapter =
//                new MarketRateDataAdapter(tradingTickerService, tradingCandlestickService);
//
//        marketRateDataAdapter.loadDataSince(previousFullHour(now))
//                .get();
//
//        verify(tradingTickerService, times(1))
//                .getMarkets();
    }

    @Test
    @Ignore
    public void should_get_BitBay_market_rates_since_last_hour() throws ExecutionException, InterruptedException {
//        TradingTickerService tradingTickerService = mock(TradingTickerService.class);
//        when(tradingTickerService.getMarkets())
//                .thenReturn(CompletableFuture.completedFuture(markets(1)));
//        TradingCandlestickService tradingCandlestickService = mock(TradingCandlestickService.class);
//        MarketRateDataAdapter marketRateDataAdapter =
//                new MarketRateDataAdapter(tradingTickerService, tradingCandlestickService);
//
//        marketRateDataAdapter.loadDataSince(previousFullHour(now))
//                .get();
//
//        verify(tradingCandlestickService, times(1))
//                .getHourlyMarketRatesSince(anyString(), any(Date.class));
    }

    private List<Market> markets(int count) {
        return Stream.iterate(market(), m -> market())
                .limit(count)
                .collect(Collectors.toList());
    }

    private Market market() {
        return new Market("BTC-PLN");
    }
}
