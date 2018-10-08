package com.jgprogram.marketrates.application;

import com.google.common.collect.ImmutableList;
import com.jgprogram.marketrates.application.dto.HourlyMarketRatesDTO;
import com.jgprogram.marketrates.application.dto.LatestMarketRateDTO;
import com.jgprogram.marketrates.application.dto.MarketRateDTO;
import com.jgprogram.marketrates.domain.model.MarketRate;
import com.jgprogram.marketrates.domain.model.MarketRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MarketRateService {

    private final MarketRateRepository marketRateRepository;

    @Autowired
    public MarketRateService(MarketRateRepository marketRateRepository) {
        this.marketRateRepository = marketRateRepository;
    }

    public void createMarketRate(MarketRateDTO marketRateDTO) {
        MarketRate existing = marketRateRepository.findByMarketCodeAndDate(
                marketRateDTO.getMarketCode(),
                marketRateDTO.getDate());

        if (existing != null) {
            return;
        }

        MarketRate marketRate = new MarketRate(
                marketRateDTO.getMarketCode(),
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

    //TODO optimisations
    public HourlyMarketRatesDTO getMarketRatesHourly(final String marketCode, final Date day, Integer[] hours) {
        if (StringUtils.isEmpty(marketCode)) {
            throw new IllegalArgumentException("A market code is required.");
        }

        if (day == null) {
            throw new IllegalArgumentException("A day is required.");
        }

        if (hours == null || hours.length == 0) {
            throw new IllegalArgumentException("At least one hour is required.");
        }

        List<HourlyMarketRatesDTO.HourRateDTO> hourlyRates = new ArrayList<>(hours.length);
        Arrays.stream(hours)
                .collect(Collectors.toMap(
                        Function.identity(),
                        h -> marketRateRepository.findByMarketCodeAndDate(marketCode, concatDayHour(day, h))
                ))
                .forEach((hour, marketRate) -> hourlyRates.add(
                        new HourlyMarketRatesDTO.HourRateDTO(hour, marketRate.average().doubleValue())));

        List<HourlyMarketRatesDTO.HourRateDTO> sortedHourlyRates = hourlyRates.stream()
                .sorted(Comparator.comparing(HourlyMarketRatesDTO.HourRateDTO::getHour))
                .collect(ImmutableList.toImmutableList());

        return new HourlyMarketRatesDTO(day, sortedHourlyRates);
    }

    private Date concatDayHour(Date day, Integer hour) {
        return Date.from(
                LocalDateTime.ofInstant(day.toInstant(), ZoneId.systemDefault())
                        .withHour(hour)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());
    }
}
