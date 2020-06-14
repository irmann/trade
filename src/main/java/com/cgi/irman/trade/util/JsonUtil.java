package com.cgi.irman.trade.util;

import com.cgi.irman.trade.Trade;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Trade asTrade(final String json) {
        try {
            return new ObjectMapper().readValue(json, Trade.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
