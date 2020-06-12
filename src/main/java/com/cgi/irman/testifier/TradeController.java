package com.cgi.irman.testifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TradeController {
    public TradeService getService() {
        return service;
    }
    @Autowired
    public void setService(TradeService service) {
        this.service = service;
    }

    private TradeService service;

    @PostMapping("/trade")
    public Response storeTrade(@RequestBody Trade trade) throws Exception {
        return service.store(trade);
    }
}

