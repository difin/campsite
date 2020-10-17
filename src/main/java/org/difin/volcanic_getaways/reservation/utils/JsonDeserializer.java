package org.difin.volcanic_getaways.reservation.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class JsonDeserializer {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, String> jsonToMap(String jsonString) {

        try {
            return objectMapper.readValue(jsonString, Map.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
