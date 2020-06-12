package com.cgi.irman.testifier;

import org.springframework.stereotype.Service;

@Service
public class TradeService {
    public Response store(Trade trade) throws Exception {
        //throw new Exception("unknown exception");
        return new Response(1, "OK", 0);
    }
}
