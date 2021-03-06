package com.cgi.irman.trade.validation;

import com.cgi.irman.trade.Trade;
import com.cgi.irman.trade.TradeRepository;
import com.cgi.irman.trade.exceptions.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.Optional;

import static com.cgi.irman.trade.util.Constants.ERROR_VALIDATION_VERSION;

public class VersionValidator extends ValidatorBase implements ValidatorInterface{

    TradeRepository tradeRepository;

    public TradeRepository getTradeRepository() {
        return tradeRepository;
    }

    @Autowired
    public void setTradeRepository(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    @Override
    public void validate(Trade trade) throws ValidatorException {
        Optional<Long> max = this.tradeRepository.findMaxVersion(trade.getTradeId());
        if(max.orElse(-1l) >= trade.getTradeVersion())
            throw new ValidatorException(ERROR_VALIDATION_VERSION, MessageFormat.format(
                    "Version must be higher then {0}", max.get()));
    }
}
