package com.jgprogram.bitbay.marketrates.port.adapter.scheduler;

import com.jgprogram.bitbay.marketrates.Specification;
import com.jgprogram.bitbay.marketrates.port.adapter.bitbay.MarketRate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jgprogram.common.util.TimeFullUnit.*;

public class MarketRateUpdateTaskSpec extends Specification {

//    @Test
//    public void when_last_rate_is_older_than_1_hour_should_ask_BitBay_CandlestickChartAdapter_for_new_data() {
//        final Date previousHour = previousHour(new Date());
//        CandlestickChartAdapter candlestickChartAdapter = mock(CandlestickChartAdapter.class);
//        when(candlestickChartAdapter.getSince(previousHour))
//                .thenReturn(candlestickChartsSince(previousHour, 1));
//        MarketRateService marketRateService = mock(MarketRateService.class);
//        when(marketRateService.latestRateDate())
//                .thenReturn(previousHour);
//        MarketRateUpdateTask marketRateUpdateTask = new MarketRateUpdateTask(marketRateService, candlestickChartAdapter);
//
//        marketRateUpdateTask.loadLatest();
//
//        verify(marketRateService, times(1))
//                .latestRateDate();
//        verify(candlestickChartAdapter, times(1))
//                .getSince(previousHour);
//        verify(marketRateService, times(1))
//                .createMarketRate(any(MarketRateDTO.class));
//
//    }
//
//    @Test
//    public void when_last_rate_is_not_older_than_1_hour_it_should_not_ask_BitBay_CandlestickChartAdapter_for_new_data() {
//        final Date currentHour = currentHour();
//        CandlestickChartAdapter candlestickChartAdapter = mock(CandlestickChartAdapter.class);
//        MarketRateService marketRateService = mock(MarketRateService.class);
//        when(marketRateService.latestRateDate())
//                .thenReturn(currentHour);
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
                nextHour(data.getDate()));
    }

    private Set<MarketRate> candlestickChartsSince(Date sinceDate, int count) {
        return Stream.iterate(candlestickChartData(sinceDate), this::candlestickChartDataNextHour)
                .limit(count)
                .collect(Collectors.toSet());
    }
}
