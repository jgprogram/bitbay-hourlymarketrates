package com.jgprogram.marketrates.application;

import com.jgprogram.marketrates.application.dto.LatestMarketRateDTO;
import com.jgprogram.marketrates.application.dto.MarketRateDTO;
import com.jgprogram.marketrates.domain.model.MarketRate;
import com.jgprogram.marketrates.domain.model.MarketRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public LatestMarketRateDTO getLatestMarketRate() {
        return new LatestMarketRateDTO(
                marketRateRepository.findLatestDate());
    }
}
