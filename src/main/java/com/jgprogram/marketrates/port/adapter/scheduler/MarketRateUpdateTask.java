package com.jgprogram.marketrates.port.adapter.scheduler;

import com.jgprogram.marketrates.application.MarketRateService;
import com.jgprogram.marketrates.application.dto.MarketRateDTO;
import com.jgprogram.marketrates.bitbay.MarketRateData;
import com.jgprogram.marketrates.bitbay.MarketRateDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class MarketRateUpdateTask {

    private static final Logger logger = LoggerFactory.getLogger(MarketRateUpdateTask.class);
    private static final Duration ONE_HOUR = Duration.ofHours(1);

    private final MarketRateService marketRateService;
    private final MarketRateDataService marketRateDataService;

    @Autowired
    public MarketRateUpdateTask(MarketRateService marketRateService, MarketRateDataService marketRateDataService) {
        this.marketRateService = marketRateService;
        this.marketRateDataService = marketRateDataService;
    }

    @Scheduled(fixedDelay = 60000) //1 min
    public void loadData() {
        Date latestRateDate = marketRateService.getLatestMarketRate().getDate();
        if(latestRateDate == null) {
            latestRateDate = new Date(1388530800000L);
        }

        if (durationSince(latestRateDate).compareTo(ONE_HOUR) > 0) {
            try {
                marketRateDataService.loadDataFromOneYear(latestRateDate).get();
            } catch (Exception e) {
                logger.error("Error until loading data from market rate data service.", e);
            }
        }
    }

    private MarketRateDTO mapToMarketRateDto(MarketRateData source) {
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
