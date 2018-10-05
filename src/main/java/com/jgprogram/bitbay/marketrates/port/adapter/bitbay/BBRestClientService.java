package com.jgprogram.bitbay.marketrates.port.adapter.bitbay;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgprogram.common.port.adapter.RestClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

abstract class BBRestClientService extends RestClientService {

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
                //TODO Handle errors
                throw new Exception("Request fail. Errors: NOT_IMPLEMENTED_YET");
            }

            return findNode("items", rootNode);
        } else {
            throw new Exception("Bad status code " + responseEntity.getStatusCode());
        }
    }
}
