package com.jgprogram.bitbay.marketrates.port.adapter.bitbay;

import com.fasterxml.jackson.databind.JsonNode;
import com.jgprogram.common.port.adapter.RestClientService;
import com.jgprogram.common.util.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
class TradingCandlestickService extends BBRestClientService {

    private static final Logger logger = LoggerFactory.getLogger(TradingCandlestickService.class);
    private static final String RESOURCE_PATH = "/trading/candle/history/%s/%s?from=%d000&to=%d000";

    @Autowired
    private Environment env;

    public TradingCandlestickService(RestTemplateBuilder restTemplateBuilder) {
        super(restTemplateBuilder.build());
    }

    @Async(value = "bibayThreadPool")
    public CompletableFuture<List<MarketRate>> getHourlyMarketRatesSince(String marketCode, Date since) throws Exception {
        final String url = String.format(
                env.getProperty("bitbay.api.url") + RESOURCE_PATH,
                marketCode,
                Resolution.ONE_HOUR.value(),
                since.toInstant().getEpochSecond(),
                Time.oneYearLater(since).toInstant().getEpochSecond());

        List<MarketRate> marketRates = mapToMarketRates(marketCode, getItemsNode(url));

        return CompletableFuture.completedFuture(marketRates);
    }

    private List<MarketRate> mapToMarketRates(String marketCode, JsonNode itemsNode) throws Exception {
        List<MarketRate> marketRates = new ArrayList<>();
        if(itemsNode.isArray()) {
            for(JsonNode itemNode : itemsNode) {
                JsonNode timeNode = findNode(0, itemNode);
                JsonNode candlestickNode = findNode(1, itemNode);
                JsonNode oNode = findNode("o", candlestickNode);
                JsonNode cNode = findNode("c", candlestickNode);
                JsonNode hNode = findNode("h", candlestickNode);
                JsonNode lNode = findNode("l", candlestickNode);
                JsonNode vNode = findNode("v", candlestickNode);

                marketRates.add(new MarketRate(
                        marketCode,
                        new Date(timeNode.asLong()),
                        oNode.asDouble(),
                        cNode.asDouble(),
                        hNode.asDouble(),
                        lNode.asDouble(),
                        vNode.asDouble()
                ));
            }
        } else {
            throw new Exception("Items node should be array.");
        }

        return Collections.unmodifiableList(marketRates);
    }

    private enum Resolution {

        HALF_HOUR("1800"),
        ONE_HOUR("3600"),
        HALF_DAY("43200"),
        ONE_DAY("86400");

        private final String value;

        Resolution(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }
}
