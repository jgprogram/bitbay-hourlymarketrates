package com.jgprogram.bitbay.marketrates.port.adapter.cache;

import com.jgprogram.bitbay.marketrates.Specification;
import com.jgprogram.bitbay.marketrates.application.MarketRateService;
import com.jgprogram.bitbay.marketrates.application.dto.MarketRateDTO;
import com.jgprogram.bitbay.marketrates.port.adapter.bitbay.MarketRateDataLoadCompleted;
import com.jgprogram.bitbay.marketrates.port.adapter.bitbay.MarketRateDataLoadErrorOccurred;
import com.jgprogram.bitbay.marketrates.port.adapter.bitbay.MarketRateDataLoaded;
import org.junit.Test;
import com.jgprogram.bitbay.marketrates.event.EventBus;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.UUID;

import static com.jgprogram.common.util.TimeFullUnit.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class MarketRateCacheSpec extends Specification {

    @Test
    public void should_receive_and_store_RateLoaded_event_with_requestId_in_cache() {
        final String requestId = UUID.randomUUID().toString();
        MarketRateDataLoaded marketRateDataLoaded = marketRateDataLoaded(requestId);
        MarketRateDataCache cache = mock(MarketRateDataCache.class);
        MarketRateDataLoadedSubscriber subscriber = new MarketRateDataLoadedSubscriber(cache);
        EventBus.instance()
                .subscribe(subscriber);

        EventBus.instance()
                .publish(marketRateDataLoaded);

        assertThat(marketRateDataLoaded.occurredOn(), is(notNullValue()));
        assertThat(marketRateDataLoaded.requestId(), is(requestId));
        verify(cache, times(1))
                .add(marketRateDataLoaded);
    }

    @Test
    public void after_MarketRateDataLoadCompleted_should_get_data_from_cache_by_requestId_and_push_it_to_MarketRateService() {
        final String requestId = UUID.randomUUID().toString();
        MarketRateDataLoadCompleted event = new MarketRateDataLoadCompleted(requestId);
        MarketRateDataLoaded expectedMarketRateDataLoaded = marketRateDataLoaded(requestId);
        MarketRateDataCache cache = mock(MarketRateDataCache.class);
        when(cache.findByRequestId(requestId))
                .thenReturn(Arrays.asList(expectedMarketRateDataLoaded));
        MarketRateService marketRateService = mock(MarketRateService.class);
        ArgumentCaptor<MarketRateDTO> marketRateDTOCaptor = ArgumentCaptor.forClass(MarketRateDTO.class);

        MarketRateDataLoadCompletedSubscriber subscriber =
                new MarketRateDataLoadCompletedSubscriber(cache, marketRateService);
        EventBus.instance()
                .subscribe(subscriber);

        EventBus.instance()
                .publish(event);

        verify(cache, times(1))
                .findByRequestId(requestId);
        verify(marketRateService, times(1))
                .createMarketRate(marketRateDTOCaptor.capture());
        final MarketRateDTO expectedMarketRateDTO = marketRateDTOCaptor.getValue();
        assertThat(expectedMarketRateDTO.getClose(), is(expectedMarketRateDataLoaded.close()));
        assertThat(expectedMarketRateDTO.getCode(), is(expectedMarketRateDataLoaded.code()));
        assertThat(expectedMarketRateDTO.getDate(), is(expectedMarketRateDataLoaded.date()));
        assertThat(expectedMarketRateDTO.getHighest(), is(expectedMarketRateDataLoaded.highest()));
        assertThat(expectedMarketRateDTO.getLowest(), is(expectedMarketRateDataLoaded.lowest()));
        assertThat(expectedMarketRateDTO.getOpen(), is(expectedMarketRateDataLoaded.open()));
    }

    @Test
    public void should_clean_cache_by_requestId_when_MarketRateDataLoadErrorOccurred() {
        final String requestId = UUID.randomUUID().toString();
        MarketRateDataLoadErrorOccurred event = new MarketRateDataLoadErrorOccurred(requestId);
        MarketRateDataCache cache = mock(MarketRateDataCache.class);
        MarketRateDataLoadErrorOccurredSubscriber subscriber =
                new MarketRateDataLoadErrorOccurredSubscriber(cache);
        EventBus.instance().subscribe(subscriber);

        EventBus.instance()
                .publish(event);

        verify(cache).removeByRequestId(requestId);
    }

    private MarketRateDataLoaded marketRateDataLoaded(String requestId) {
        return new MarketRateDataLoaded(
                requestId,
                "BTC-PLN",
                currentHour(),
                51280D,
                50400D,
                51280D,
                50200D
        );
    }
}
