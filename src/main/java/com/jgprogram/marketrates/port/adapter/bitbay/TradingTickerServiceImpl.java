package com.jgprogram.marketrates.port.adapter.bitbay;

import com.fasterxml.jackson.databind.JsonNode;
import com.jgprogram.marketrates.bitbay.Market;
import com.jgprogram.marketrates.bitbay.TradingTickerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
class TradingTickerServiceImpl extends BBRestClientService implements TradingTickerService {

    private static final Logger logger = LoggerFactory.getLogger(TradingTickerServiceImpl.class);
    private static final String RESOURCE_PATH = "/trading/ticker";

    @Autowired
    private Environment env;

    public TradingTickerServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        super(restTemplateBuilder.build());
    }

    @Override
    @Async(value = "bibayThreadPool")
    public CompletableFuture<List<Market>> getMarkets() throws Exception {
        final String url = env.getProperty("bitbay.api.url") + RESOURCE_PATH;
        List<Market> markets = mapToMarkets(getItemsNode(url));

        return CompletableFuture.completedFuture(markets);
    }

    private List<Market> mapToMarkets(JsonNode itemsNode) throws Exception {
        List<Market> markets = new ArrayList<>();

        Iterator<Map.Entry<String, JsonNode>> itemsIter = itemsNode.fields();
        while (itemsIter.hasNext()) {
            JsonNode itemNode = itemsIter.next().getValue();
            JsonNode marketNode = findNode("market", itemNode);

            JsonNode marketCodeNode = findNode("code", marketNode);
            markets.add(new Market(marketCodeNode.asText()));
        }

        return Collections.unmodifiableList(markets);
    }
}
