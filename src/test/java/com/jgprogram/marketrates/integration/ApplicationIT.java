package com.jgprogram.marketrates.integration;

import com.jgprogram.common.util.TimeFullUnit;
import com.jgprogram.marketrates.IntegrationTestCase;
import com.jgprogram.marketrates.bitbay.*;
import com.jgprogram.marketrates.port.adapter.spring.EventBusInitializer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.*;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"inMemoryRepositories"})
public class ApplicationIT extends IntegrationTestCase {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EventBusInitializer eventBusInitializer;

    @Test
    public void should_find_market_rate_by_date_and_hours() throws Exception {
        setup();
        String marketCode = "BTC-PLN";
        String day = "2017-01-01";
        int[] hours = {8, 12, 16};

        mockMvc.perform(get(buildMarketRatesUrl(marketCode, day, hours))
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.day", is(day)))
                .andExpect(jsonPath("$.hours[0].hour", is(8)))
                .andExpect(jsonPath("$.hours[0].avgRate", is(8.00)))
                .andExpect(jsonPath("$.hours[1].hour", is(12)))
                .andExpect(jsonPath("$.hours[1].avgRate", is(12.00)))
                .andExpect(jsonPath("$.hours[2].hour", is(16)))
                .andExpect(jsonPath("$.hours[2].avgRate", is(16.00)));
    }

    private void setup() throws Exception {
        eventBusInitializer.onAppReady();
        prepareData();
    }

    private String buildMarketRatesUrl(String marketCode, String day, int[] hours) {
        StringBuilder sb = new StringBuilder();
        sb.append("/markets");
        sb.append("/" + marketCode);
        sb.append("/rates");
        sb.append("/" + day);
        sb.append("?hours=");
        sb.append(hours[0]);

        if (hours.length > 1) {
            for (int i = 1; i < hours.length; i++) {
                sb.append("&hours=");
                sb.append(hours[i]);
            }
        }

        return sb.toString();
    }

    private void prepareData() throws Exception {
        final String marketCode = "BTC-PLN";
        final Date dateSince = dateOf(2017, Month.JANUARY, 1, 0, 0, 0);
        final Date dateTo = TimeFullUnit.oneYearLater(dateSince);

        TradingTickerService tradingTickerService = mock(TradingTickerService.class);
        when(tradingTickerService.getMarkets())
                .thenReturn(CompletableFuture.completedFuture(
                        Arrays.asList(new MarketData(marketCode))));



        TradingCandlestickService tradingCandlestickService = mock(TradingCandlestickService.class);
        when(tradingCandlestickService.getHourlyMarketRatesSince(marketCode, dateSince, dateTo))
                .thenReturn(CompletableFuture.completedFuture(
                        Arrays.asList(
                                new MarketRateData(
                                        marketCode,
                                        dateOf(2017, Month.JANUARY, 1, 8, 0, 0),
                                        8D,8D, 8D, 8D, 8D
                                ),
                                new MarketRateData(
                                        marketCode,
                                        dateOf(2017, Month.JANUARY, 1, 12, 0, 0),
                                        12D,12D, 12D, 12D, 12D
                                ),
                                new MarketRateData(
                                        marketCode,
                                        dateOf(2017, Month.JANUARY, 1, 16, 0, 0),
                                        16D,16D, 16D, 16D, 16D
                                )
                        )
                ));

        MarketRateDataService marketRateDataService = new MarketRateDataService(tradingTickerService, tradingCandlestickService);
        marketRateDataService.loadDataFromOneYear(dateSince).get();
    }


    private Date dateOf(int y, Month m, int d, int hh, int mm, int ss) {
        return Date.from(
                LocalDateTime.of(y, m, d, hh, mm, ss)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());
    }
}
