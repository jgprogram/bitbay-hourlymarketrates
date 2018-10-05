package com.jgprogram.common.port.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.client.RestTemplate;

public abstract class RestClientService {

    private final RestTemplate restTemplate;

    public RestClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected JsonNode findNode(String path, JsonNode parentNode) throws Exception {
        JsonNode node = parentNode.path(path);
        if (node.isMissingNode()) {
            throw new Exception("Node " + path + "not found.");
        }

        return node;
    }

    protected JsonNode findNode(int index, JsonNode parentNode) throws Exception {
        JsonNode node = parentNode.path(index);
        if (node.isMissingNode()) {
            throw new Exception("Node " + index + " not found.");
        }

        return node;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
