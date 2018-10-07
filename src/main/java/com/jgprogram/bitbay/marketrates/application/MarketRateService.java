package com.jgprogram.bitbay.marketrates.application;

import com.jgprogram.bitbay.marketrates.application.dto.MarketRateDTO;
import com.jgprogram.bitbay.marketrates.domain.model.MarketRate;
import com.jgprogram.bitbay.marketrates.domain.model.MarketRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MarketRateService {

    private final MarketRateRepository marketRateRepository;

    @Autowired
    public MarketRateService(MarketRateRepository marketRateRepository) {
        this.marketRateRepository = marketRateRepository;
    }

    public void createMarketRate(MarketRateDTO marketRateDTO) {
        MarketRate marketRate = new MarketRate(
                marketRateDTO.getCode(),
                marketRateDTO.getDate(),
                marketRateDTO.getOpen(),
                marketRateDTO.getClose(),
                marketRateDTO.getHighest(),
                marketRateDTO.getLowest()
        );

        marketRateRepository.add(marketRate);
    }

    public Date latestRateDate() {
        throw new UnsupportedOperationException("Unimplemented yet.");
    }
}