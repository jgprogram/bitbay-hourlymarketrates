package com.jgprogram.bitbay.marketrates.port.adapter.task;

import com.jgprogram.bitbay.marketrates.application.MarketRateService;
import com.jgprogram.bitbay.marketrates.port.adapter.bitbay.CandlestickChartAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MarketRateUpdateTask {

    private final CandlestickChartAdapter candlestickChartAdapter;
    private final MarketRateService marketRateService;

    @Autowired
    public MarketRateUpdateTask(MarketRateService marketRateService, CandlestickChartAdapter candlestickChartAdapter) {
        this.candlestickChartAdapter = candlestickChartAdapter;
        this.marketRateService = marketRateService;
    }

    @Scheduled(fixedDelay = 60000)
    public void loadLatest() {
    }
}
