package com.cgi.irman.trade.validation;

import com.cgi.irman.trade.Trade;
import com.cgi.irman.trade.TradeDao;
import com.cgi.irman.trade.exceptions.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.Optional;

import static com.cgi.irman.trade.util.Constants.ERROR_VALIDATION_VERSION;

public class VersionValidator extends ValidatorBase implements ValidatorInterface{

    TradeDao tradeDao;

    public TradeDao getTradeDao() {
        return tradeDao;
    }

    @Autowired
    public void setTradeDao(TradeDao tradeDao) {
        this.tradeDao = tradeDao;
    }

    @Override
    public void validate(Trade trade) throws ValidatorException {
        Optional<Long> max = this.tradeDao.findMaxVersion(trade.getTradeId());
        if(max.orElse(-1l) >= trade.getTradeVersion())
            throw new ValidatorException(ERROR_VALIDATION_VERSION, MessageFormat.format(
                    "version must be more then {0}", max.get()));
    }
}