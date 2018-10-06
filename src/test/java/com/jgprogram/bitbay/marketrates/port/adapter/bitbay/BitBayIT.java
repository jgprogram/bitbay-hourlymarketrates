package com.jgprogram.bitbay.marketrates.port.adapter.bitbay;

import com.jgprogram.bitbay.marketrates.IntegrationTestCase;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.jgprogram.common.util.Time.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class BitBayIT extends IntegrationTestCase {
    private static final Logger logger = LoggerFactory.getLogger(MarketRateDataAdapter.class);

    @Autowired
    TradingCandlestickService tradingCandlestickService;

    @Autowired
    TradingTickerService tradingTickerService;

    @Test
    public void should_get_market_rate_from_previous_hour() throws Exception {
        final Date previousFullHour = previousFullHour(now());
        List<Market> markets = tradingTickerService.getMarkets().get();
        assertNotNull(markets);
        assertThat(markets, is(not(empty())));

        Market market = markets.get(0);
        assertThat(market.getCode(), not(isEmptyOrNullString()));

        List<MarketRate> marketRates = tradingCandlestickService
                .getHourlyMarketRatesSince(market.getCode(), previousFullHour).get();
        assertNotNull(marketRates);
        assertThat(marketRates, is(not(empty())));

        MarketRate marketRate = marketRates.get(0);
        assertThat(marketRate.getCode(), is(market.getCode()));
        assertThat(marketRate.getDate(), is(previousFullHour));
        assertThat(marketRate.getC(), is(greaterThanOrEqualTo(0D)));
        assertThat(marketRate.getL(), is(greaterThanOrEqualTo(0D)));
        assertThat(marketRate.getH(), is(greaterThanOrEqualTo(0D)));
        assertThat(marketRate.getO(), is(greaterThanOrEqualTo(0D)));
        assertThat(marketRate.getV(), is(greaterThanOrEqualTo(0D)));
    }

    @Test
    public void should_get_all_market_rates() throws Exception {
        final Date previousFullHour = previousFullHour(now());

        List<Market> markets = tradingTickerService.getMarkets().get();
        assertNotNull(markets);
        assertThat(markets, is(not(empty())));

        List<CompletableFuture<List<MarketRate>>> marketRateRequests = new ArrayList<>(markets.size());
        for (Market market : markets) {
            CompletableFuture<List<MarketRate>> task = tradingCandlestickService
                    .getHourlyMarketRatesSince(market.getCode(), previousFullHour);
            marketRateRequests.add(task);
        }

        int marketRatesRequestCompleted = 0;
        for (CompletableFuture<List<MarketRate>> request : marketRateRequests) {
            List<MarketRate> marketRates = request.get();
            marketRatesRequestCompleted++;
        }

        assertThat(markets.size(), is(marketRatesRequestCompleted));
    }
}
