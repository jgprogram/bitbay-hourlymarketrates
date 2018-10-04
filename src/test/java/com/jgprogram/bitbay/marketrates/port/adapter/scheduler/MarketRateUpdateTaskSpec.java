package com.jgprogram.bitbay.marketrates.port.adapter.scheduler;

import com.jgprogram.bitbay.marketrates.application.MarketRateService;
import com.jgprogram.bitbay.marketrates.application.dto.MarketRateDTO;
import com.jgprogram.bitbay.marketrates.domain.model.MarketRate;
import com.jgprogram.bitbay.marketrates.port.adapter.bitbay.CandlestickChartData;
import com.jgprogram.bitbay.marketrates.port.adapter.bitbay.CandlestickChartAdapter;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

public class MarketRateUpdateTaskSpec {

    @Test
    public void when_last_rate_is_older_than_1_hour_should_ask_BitBay_CandlestickChartAdapter_for_new_data() {
        final Date previousFullHour = previousFullHour(new Date());
        CandlestickChartAdapter candlestickChartAdapter = mock(CandlestickChartAdapter.class);
        when(candlestickChartAdapter.getSince(previousFullHour))
                .thenReturn(candlestickChartsSince(previousFullHour, 1));
        MarketRateService marketRateService = mock(MarketRateService.class);
        when(marketRateService.latestRateDate())
                .thenReturn(previousFullHour);
        MarketRateUpdateTask marketRateUpdateTask = new MarketRateUpdateTask(marketRateService, candlestickChartAdapter);

        marketRateUpdateTask.loadLatest();

        verify(marketRateService, times(1))
                .latestRateDate();
        verify(candlestickChartAdapter, times(1))
                .getSince(previousFullHour);
        verify(marketRateService, times(1))
                .createMarketRate(any(MarketRateDTO.class));

    }

    @Test
    public void when_last_rate_is_not_older_than_1_hour_it_should_not_ask_BitBay_CandlestickChartAdapter_for_new_data() {
        final Date currentFullHour = currentFullHour();
        CandlestickChartAdapter candlestickChartAdapter = mock(CandlestickChartAdapter.class);
        MarketRateService marketRateService = mock(MarketRateService.class);
        when(marketRateService.latestRateDate())
                .thenReturn(currentFullHour);
        MarketRateUpdateTask marketRateUpdateTask = new MarketRateUpdateTask(marketRateService, candlestickChartAdapter);

        marketRateUpdateTask.loadLatest();

        verifyZeroInteractions(candlestickChartAdapter);
        verify(marketRateService, times(0))
                .createMarketRate(any(MarketRateDTO.class));
    }

    private Date currentFullHour() {
        return Date.from(
                LocalDateTime.now()
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());

    }

    private Date previousFullHour(Date current) {
        return Date.from(
                LocalDateTime.ofInstant(current.toInstant(), ZoneId.systemDefault())
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0)
                        .minusHours(1)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());

    }

    private Date nextFullHour(Date current) {
        return Date.from(
                LocalDateTime.ofInstant(current.toInstant(), ZoneId.systemDefault())
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0)
                        .plusHours(1)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());

    }

    private MarketRate marketRateWithDate(LocalDateTime dateTime) {
        return new MarketRate(
                "BTC-PLN",
                Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()),
                25045.87,
                25046.03,
                25130.93,
                25043.59
        );
    }

    private CandlestickChartData candlestickChartData(Date date) {
        return new CandlestickChartData(
                "BTC-PLN",
                date,
                25045.87,
                25046.03,
                25130.93,
                25043.59,
                0.22349329);
    }

    private CandlestickChartData candlestickChartDataNextHour(CandlestickChartData data) {
        return candlestickChartData(
                nextFullHour(data.getDate()));
    }

    private Set<CandlestickChartData> candlestickChartsSince(Date sinceDate, int count) {
        return Stream.iterate(candlestickChartData(sinceDate), this::candlestickChartDataNextHour)
                .limit(count)
                .collect(Collectors.toSet());
    }
}
