package com.cgi.irman.trade.validation;

import com.cgi.irman.trade.Trade;
import com.cgi.irman.trade.TradeModel;
import com.cgi.irman.trade.TradeRepository;
import com.cgi.irman.trade.exceptions.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.cgi.irman.trade.util.Constants.ERROR_VALIDATION_EXPIRED;

public class ExpiredValidator implements ValidatorInterface{

    TradeRepository tradeRepository;

    public TradeRepository getTradeRepository() {
        return tradeRepository;
    }

    @Autowired
    public void setTradeRepository(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }


    public void validate(Trade trade) throws ValidatorException {
        Optional<TradeModel> tradeModelO = tradeRepository.findLastVersionByTradId(trade.getTradeId());
        if (tradeModelO.isPresent() && tradeModelO.get().getExpired())
            throw new ValidatorException(ERROR_VALIDATION_EXPIRED, "trade is already expired");
    }
}
