package com.jgprogram.bitbay.marketrates.port.adapter.scheduler;

import com.jgprogram.bitbay.marketrates.Specification;
import com.jgprogram.bitbay.marketrates.port.adapter.bitbay.MarketRate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jgprogram.common.util.Time.*;

public class MarketRateUpdateTaskSpec extends Specification {

//    @Test
//    public void when_last_rate_is_older_than_1_hour_should_ask_BitBay_CandlestickChartAdapter_for_new_data() {
//        final Date previousFullHour = previousFullHour(new Date());
//        CandlestickChartAdapter candlestickChartAdapter = mock(CandlestickChartAdapter.class);
//        when(candlestickChartAdapter.getSince(previousFullHour))
//                .thenReturn(candlestickChartsSince(previousFullHour, 1));
//        MarketRateService marketRateService = mock(MarketRateService.class);
//        when(marketRateService.latestRateDate())
//                .thenReturn(previousFullHour);
//        MarketRateUpdateTask marketRateUpdateTask = new MarketRateUpdateTask(marketRateService, candlestickChartAdapter);
//
//        marketRateUpdateTask.loadLatest();
//
//        verify(marketRateService, times(1))
//                .latestRateDate();
//        verify(candlestickChartAdapter, times(1))
//                .getSince(previousFullHour);
//        verify(marketRateService, times(1))
//                .createMarketRate(any(MarketRateDTO.class));
//
//    }
//
//    @Test
//    public void when_last_rate_is_not_older_than_1_hour_it_should_not_ask_BitBay_CandlestickChartAdapter_for_new_data() {
//        final Date currentFullHour = currentFullHour();
//        CandlestickChartAdapter candlestickChartAdapter = mock(CandlestickChartAdapter.class);
//        MarketRateService marketRateService = mock(MarketRateService.class);
//        when(marketRateService.latestRateDate())
//                .thenReturn(currentFullHour);
//        MarketRateUpdateTask marketRateUpdateTask = new MarketRateUpdateTask(marketRateService, candlestickChartAdapter);
//
//        marketRateUpdateTask.loadLatest();
//
//        verifyZeroInteractions(candlestickChartAdapter);
//        verify(marketRateService, times(0))
//                .createMarketRate(any(MarketRateDTO.class));
//    }

    private com.jgprogram.bitbay.marketrates.domain.model.MarketRate marketRateWithDate(LocalDateTime dateTime) {
        return new com.jgprogram.bitbay.marketrates.domain.model.MarketRate(
                "BTC-PLN",
                Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()),
                25045.87,
                25046.03,
                25130.93,
                25043.59
        );
    }

    private MarketRate candlestickChartData(Date date) {
        return new MarketRate(
                "BTC-PLN",
                date,
                25045.87,
                25046.03,
                25130.93,
                25043.59,
                0.22349329);
    }

    private MarketRate candlestickChartDataNextHour(MarketRate data) {
        return candlestickChartData(
                nextFullHour(data.getDate()));
    }

    private Set<MarketRate> candlestickChartsSince(Date sinceDate, int count) {
        return Stream.iterate(candlestickChartData(sinceDate), this::candlestickChartDataNextHour)
                .limit(count)
                .collect(Collectors.toSet());
    }
}
