package com.jgprogram.bitbay.marketrates.application;

import com.jgprogram.bitbay.marketrates.application.dto.LatestMarketRateDTO;
import com.jgprogram.bitbay.marketrates.application.dto.MarketRateDTO;
import com.jgprogram.bitbay.marketrates.domain.model.MarketRate;
import com.jgprogram.bitbay.marketrates.domain.model.MarketRateRepository;
import com.jgprogram.common.util.TimeFullUnit;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MarketRateSpec {

    @Test
    public void should_create_new_market_rate() {
        MarketRateDTO marketRateDTO = marketRateDTO();
        MarketRate expectedMarketRate = marketRateFromDTO(marketRateDTO);
        MarketRateRepository marketRateRepository = mock(MarketRateRepository.class);
        MarketRateService service = new MarketRateService(marketRateRepository);

        service.createMarketRate(marketRateDTO);

        verify(marketRateRepository, times(1))
                .add(any(MarketRate.class));
        assertThat(expectedMarketRate.code(), is(marketRateDTO.getCode()));
        assertThat(expectedMarketRate.date(), is(marketRateDTO.getDate()));
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

    private MarketRate marketRateFromDTO(MarketRateDTO marketRateDTO) {
        return new MarketRate(
                marketRateDTO.getCode(),
                marketRateDTO.getDate(),
                marketRateDTO.getOpen(),
                marketRateDTO.getClose(),
                marketRateDTO.getHighest(),
                marketRateDTO.getLowest()
        );
    }

    private MarketRateDTO marketRateDTO() {
        String code = "BTC-PLN";
        Date date = new Date(1538352000000L);
        Double open = 51280D;
        Double close = 50400D;
        Double highest = 51280D;
        Double lowest = 50200D;
        return new MarketRateDTO(code, date, open, close, highest, lowest);
    }
}
