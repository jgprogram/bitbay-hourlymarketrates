package com.jgprogram.marketrates.port.adapter.scheduler;

import com.jgprogram.marketrates.application.dto.MarketRateDTO;
import com.jgprogram.marketrates.bitbay.MarketRate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class MarketRateUpdateTask {

    private static final Duration ONE_HOUR = Duration.ofHours(1);

//    private final CandlestickChartAdapter candlestickChartAdapter;
//    private final MarketRateService marketRateService;
//
//    @Autowired
//    public MarketRateUpdateTask(MarketRateService marketRateService, CandlestickChartAdapter candlestickChartAdapter) {
//        this.candlestickChartAdapter = candlestickChartAdapter;
//        this.marketRateService = marketRateService;
//    }
//
//    @Scheduled(fixedDelay = 1800000) //30 min
//    public void loadLatest() {
//        final Date latestRateDate = marketRateService.latestRateDate();
//        if (durationSince(latestRateDate).compareTo(ONE_HOUR) > 0) {
//            candlestickChartAdapter.getSince(latestRateDate)
//                    .stream()
//                    .map(this::mapToMarketRateDto)
//                    .forEach(marketRateService::createMarketRate);
//        }
//    }

    private MarketRateDTO mapToMarketRateDto(MarketRate source) {
        return new MarketRateDTO(
                source.getCode(),
                source.getDate(),
                source.getO(),
                source.getC(),
                source.getH(),
                source.getL());
    }

    private Duration durationSince(Date date) {
        LocalDateTime latestRateDate = LocalDateTime.ofInstant(
                date.toInstant(),
                ZoneId.systemDefault());

        return Duration.between(latestRateDate, LocalDateTime.now());
    }
}
