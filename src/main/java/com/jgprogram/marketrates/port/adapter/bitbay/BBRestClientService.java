package com.jgprogram.marketrates.port.adapter.bitbay;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgprogram.common.port.adapter.RestClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static com.jgprogram.common.util.TimeFullUnit.millisToNextMinute;

abstract class BBRestClientService extends RestClientService {

    private static final Logger logger = LoggerFactory.getLogger(com.jgprogram.marketrates.port.adapter.bitbay.TradingCandlestickService.class);
    private static final int LIMIT_OF_RECONNECT_ATTEMPTS = 3;

    private final ThreadLocal<Integer> reconnectAttempts = ThreadLocal.withInitial(() -> 0);

    BBRestClientService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    JsonNode getItemsNode(String url) throws Exception {
        ResponseEntity<String> responseEntity = getRestTemplate().getForEntity(url, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            ObjectMapper responseMapper = new ObjectMapper();

            JsonNode rootNode = responseMapper.readTree(responseEntity.getBody());
            JsonNode nodeStatus = findNode("status", rootNode);

            if (!nodeStatus.asText("Fail").equals("Ok")) {
                JsonNode errorsNode = findNode("errors", rootNode);
                if (errorsNode.isArray()) {
                    for (JsonNode errorNode : errorsNode) {
                        ErrorStatus errorStatus = mapToErrorStatus(errorNode.asText());
                        if (errorStatus == ErrorStatus.ACTION_LIMIT_EXCEEDED
                                && reconnectAttempts.get() < LIMIT_OF_RECONNECT_ATTEMPTS) {
                            reconnectAttempts.set(reconnectAttempts.get() + 1);
                            Thread.sleep(millisToNextMinute());

                            return getItemsNode(url);
                        } else {
                            throw new Exception("Request fail because " + errorStatus);
                        }
                    }
                } else {
                    throw new Exception("Request fail. There is no errors array.");
                }
            }

            return findNode("items", rootNode);
        } else {
            throw new Exception("Bad status code " + responseEntity.getStatusCode());
        }
    }

    private ErrorStatus mapToErrorStatus(String errorAsText) {
        try {
            return ErrorStatus.valueOf(errorAsText);
        } catch (IllegalArgumentException ex) {
            logger.warn("Error status " + errorAsText + " not handled.");
            return ErrorStatus.OTHER;
        }
    }

    private enum ErrorStatus {
        PERMISSIONS_NOT_SUFFICIENT,
        INVALID_HASH_SIGNATURE,
        RESPONSE_TIMEOUT,
        TIMEOUT,
        ACTION_BLOCKED,
        ACTION_LIMIT_EXCEEDED,
        OTHER
    }
}
