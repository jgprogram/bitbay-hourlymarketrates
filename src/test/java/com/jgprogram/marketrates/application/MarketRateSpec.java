package com.jgprogram.marketrates.application;

import com.jgprogram.marketrates.application.dto.LatestMarketRateDTO;
import com.jgprogram.marketrates.application.dto.MarketRateDTO;
import com.jgprogram.marketrates.domain.model.MarketRate;
import com.jgprogram.marketrates.domain.model.MarketRateRepository;
import com.jgprogram.common.util.TimeFullUnit;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MarketRateSpec {

    @Test
    public void should_create_new_market_rate() {
        final Date date = Date.from(
                LocalDateTime.parse("2018-10-01T12:00:00").atZone(ZoneId.systemDefault()).toInstant());
        final Date day = Date.from(
                LocalDateTime.parse("2018-10-01T00:00:00").atZone(ZoneId.systemDefault()).toInstant());
        final Integer hour = 12;
        MarketRateDTO marketRateDTO = marketRateDTO("BTC-PLN", date);
        MarketRate expectedMarketRate = marketRateFromDTO(marketRateDTO);
        MarketRateRepository marketRateRepository = mock(MarketRateRepository.class);
        MarketRateService service = new MarketRateService(marketRateRepository);

        service.createMarketRate(marketRateDTO);

        verify(marketRateRepository, times(1))
                .add(any(MarketRate.class));
        assertThat(expectedMarketRate.marketCode(), is(marketRateDTO.getMarketCode()));
        assertThat(expectedMarketRate.date(), is(marketRateDTO.getDate()));
        assertThat(expectedMarketRate.day(), is(day));
        assertThat(expectedMarketRate.hour(), is(hour));
        assertThat(expectedMarketRate.open(), is(BigDecimal.valueOf(marketRateDTO.getOpen())));
        assertThat(expectedMarketRate.close(), is(BigDecimal.valueOf(marketRateDTO.getClose())));
        assertThat(expectedMarketRate.highest(), is(BigDecimal.valueOf(marketRateDTO.getHighest())));
        assertThat(expectedMarketRate.lowest(), is(BigDecimal.valueOf(marketRateDTO.getLowest())));
        assertThat(expectedMarketRate.average(),
                is(
                        expectedMarketRate.open()
                                .add(expectedMarketRate.close())
                                .divide(BigDecimal.valueOf(2), ROUND_HALF_UP)
                ));
    }

    @Test
    public void should_get_latest_market_date() {
        Date lastDate = TimeFullUnit.previousHour(TimeFullUnit.currentHour());
        MarketRateRepository marketRateRepository = mock(MarketRateRepository.class);
        when(marketRateRepository.findLatestDate())
                .thenReturn(lastDate);
        MarketRateService service = new MarketRateService(marketRateRepository);

        LatestMarketRateDTO latestMarketRateDTO = service.getLatestMarketRate();

        assertThat(latestMarketRateDTO.getDate(), is(lastDate));
    }

    @Test
    public void should_reject_new_market_rate_if_already_exits_rate_with_same_market_code_and_date() {
        Date date = TimeFullUnit.currentHour();
        String marketCode = "BTC-PLN";
        MarketRateRepository marketRateRepository = mock(MarketRateRepository.class);
        when(marketRateRepository.findByMarketCodeAndDate(marketCode, date))
                .thenReturn(marketRate(marketCode, date));
        MarketRateService service = new MarketRateService(marketRateRepository);

        service.createMarketRate(marketRateDTO(marketCode, date));

        verify(marketRateRepository, times(0))
                .add(any(MarketRate.class));
    }

    private MarketRate marketRateFromDTO(MarketRateDTO marketRateDTO) {
        return new MarketRate(
                marketRateDTO.getMarketCode(),
                marketRateDTO.getDate(),
                marketRateDTO.getOpen(),
                marketRateDTO.getClose(),
                marketRateDTO.getHighest(),
                marketRateDTO.getLowest()
        );
    }

    private MarketRateDTO marketRateDTO(String marketCode, Date date) {
        Double open = 51280D;
        Double close = 50400D;
        Double highest = 51280D;
        Double lowest = 50200D;
        return new MarketRateDTO(marketCode, date, open, close, highest, lowest);
    }

    private MarketRate marketRate(String marketCode, Date date) {
        Double open = 51280D;
        Double close = 50400D;
        Double highest = 51280D;
        Double lowest = 50200D;
        return new MarketRate(marketCode, date, open, close, highest, lowest);
    }
}
