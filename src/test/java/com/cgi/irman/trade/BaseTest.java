package com.cgi.irman.trade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseTest {

    protected TradeDTO getTrade() {
        return new TradeDTO("T1", 1l, "CP-1", "B1", "12/06/2020", false);
    }

    protected TradeModel getTradeModel(TradeDTO trade) throws ParseException {
        return new TradeModel(trade.getTradeId(), trade.getTradeVersion(),
                trade.getCountryPartyId(), trade.getBookId(),
                new SimpleDateFormat("dd/MM/yyyy").parse(trade.getMaturityDate()),
                new Date(), false);
    }
}
