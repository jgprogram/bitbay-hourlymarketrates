package com.jgprogram.bitbay.marketrates.port.adapter.bitbay;

import com.jgprogram.bitbay.marketrates.IntegrationTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static com.jgprogram.common.util.Time.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class BitBayIT extends IntegrationTestCase {
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
        assertThat(marketRates, hasSize(1));

        MarketRate marketRate = marketRates.get(0);
        assertThat(marketRate.getCode(), is(market.getCode()));
        assertThat(marketRate.getDate(), is(previousFullHour));
        assertThat(marketRate.getC(), is(greaterThan(0D)));
        assertThat(marketRate.getL(), is(greaterThan(0D)));
        assertThat(marketRate.getH(), is(greaterThan(0D)));
        assertThat(marketRate.getO(), is(greaterThan(0D)));
        assertThat(marketRate.getV(), is(greaterThan(0D)));
    }
}
