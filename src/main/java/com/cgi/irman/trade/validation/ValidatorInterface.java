package com.cgi.irman.trade.validation;

import com.cgi.irman.trade.Trade;
import com.cgi.irman.trade.exceptions.ValidatorException;

public interface ValidatorInterface {
    public void validate(Trade trade) throws ValidatorException;
}
