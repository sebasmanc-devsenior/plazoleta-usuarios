package com.plazoleta.users;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Base64;

final class JwtsTestSupport {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private JwtsTestSupport() {}

    static JsonNode claims(String jwt) throws Exception {
        String payload = jwt.split("\\.")[1];
        return MAPPER.readTree(Base64.getUrlDecoder().decode(payload));
    }
}
