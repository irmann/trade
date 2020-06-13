package com.cgi.irman.testifier;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseTest {

    protected Trade getTrade() {
        return new Trade("T1", 1l, "CP-1", "B1", "12/06/2020");
    }

    protected TradeModel getTradeModel(Trade trade) throws ParseException {
        return new TradeModel(trade.getTradeId(), trade.getTradeVersion(),
                trade.getCountryPartyId(), trade.getBookId(),
                new SimpleDateFormat("dd/MM/yyyy").parse(trade.getMaturityDate()),
                new Date(), false);
    }
}
