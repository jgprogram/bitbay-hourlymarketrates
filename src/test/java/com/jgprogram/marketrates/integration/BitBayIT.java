package com.jgprogram.marketrates.integration;

import com.jgprogram.marketrates.IntegrationTestCase;
import com.jgprogram.marketrates.bitbay.Market;
import com.jgprogram.marketrates.bitbay.MarketRate;
import com.jgprogram.marketrates.bitbay.TradingCandlestickService;
import com.jgprogram.marketrates.bitbay.TradingTickerService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.jgprogram.common.util.TimeFullUnit.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class BitBayIT extends IntegrationTestCase {

    @Autowired
    TradingCandlestickService tradingCandlestickService;

    @Autowired
    TradingTickerService tradingTickerService;

    @Test
    public void should_get_rates_from_market_since_previous_hour() throws Exception {
        final Date now = currentHour();
        final Date previousFullHour = previousHour(now);
        List<Market> markets = tradingTickerService.getMarkets().get();
        assertNotNull(markets);
        assertThat(markets, is(not(empty())));

        Market market = markets.get(0);
        assertThat(market.getCode(), not(isEmptyOrNullString()));

        List<MarketRate> marketRates = tradingCandlestickService
                .getHourlyMarketRatesSince(market.getCode(), previousFullHour, now).get();
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
    public void should_get_hourly_rates_from_all_available_market_since_previous_full_hour_to_current_full_hour() throws Exception {
        final Date previousFullHour = previousHour(currentHour());
        final Date currentFullHour = currentHour();

        List<Market> markets = tradingTickerService.getMarkets().get();
        assertNotNull(markets);
        assertThat(markets, is(not(empty())));

        List<CompletableFuture<List<MarketRate>>> marketRateRequests = new ArrayList<>(markets.size());
        for (Market market : markets) {
            CompletableFuture<List<MarketRate>> task = tradingCandlestickService
                    .getHourlyMarketRatesSince(market.getCode(), previousFullHour, currentFullHour);
            marketRateRequests.add(task);
        }

        int marketRatesRequestCompleted = 0;
        for (CompletableFuture<List<MarketRate>> request : marketRateRequests) {
            List<MarketRate> marketRates = request.get();
            marketRatesRequestCompleted++;

            assertNotNull(marketRates);
            assertThat(marketRates, is(not(empty())));
            assertTrue(marketRates.get(0).getDate().equals(previousFullHour));
            if(marketRates.size() == 2) { //Rates from current full hour could be not available yet.
                assertTrue(marketRates.get(1).getDate().equals(currentFullHour));
            }
        }

        assertThat(markets.size(), is(marketRatesRequestCompleted));
    }

    @Test
    public void should_get_6856_BTC_USD_rates_from_2017() throws Exception {
        final Date date_2017_01_01_0000 = new Date(1483225200000L);
        final Date oneYearLater = oneYearLater(date_2017_01_01_0000);
        final int availableRatesIn2017 = 6856;
        String marketCode = "BTC-USD";

        List<MarketRate> marketRates = tradingCandlestickService
                .getHourlyMarketRatesSince(marketCode, date_2017_01_01_0000, oneYearLater).get();
        assertNotNull(marketRates);
        assertThat(marketRates, hasSize(availableRatesIn2017));

        MarketRate firstMarketRate = marketRates.get(0);
        assertThat(firstMarketRate.getCode(), is(marketCode));
        assertThat(firstMarketRate.getDate(), is(date_2017_01_01_0000));
        MarketRate lastMarketRate = marketRates.get(availableRatesIn2017 - 1);
        assertThat(lastMarketRate.getCode(), is(marketCode));
        assertThat(lastMarketRate.getDate(), is(oneYearLater));
    }
}
