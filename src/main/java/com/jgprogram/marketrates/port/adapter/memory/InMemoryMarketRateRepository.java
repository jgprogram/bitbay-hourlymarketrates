package com.jgprogram.marketrates.port.adapter.memory;

import com.jgprogram.marketrates.domain.model.MarketRate;
import com.jgprogram.marketrates.domain.model.MarketRateRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Repository
@Profile("inMemoryRepositories")
public class InMemoryMarketRateRepository implements MarketRateRepository {

    private final List<MarketRate> rates = new ArrayList<>();

    @Override
    public void add(MarketRate marketRate) {
        rates.add(marketRate);
    }

    @Override
    public Date findLatestDate() {
        if (rates.isEmpty()) {
            return null;
        }

        return rates.stream()
                .sorted(Comparator.comparing(MarketRate::date))
                .skip(rates.size() - 1)
                .map(MarketRate::date)
                .findFirst()
                .orElse(null);
    }

    @Override
    public MarketRate findByMarketCodeAndDate(String marketCode, Date date) {
        return rates.stream()
                .filter(r -> r.marketCode().equals(marketCode) && r.date().equals(date))
                .findFirst()
                .orElse(null);
    }

    @Override
    public MarketRate findByMarketCodeAndDayAndHour(String marketCode, Date day, Integer hour) {
        return rates.stream()
                .filter(r -> r.marketCode().equals(marketCode)
                        && r.day().equals(day)
                        && r.hour().equals(hour))
                .findFirst()
                .orElse(null);
    }
}
