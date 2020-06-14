package com.cgi.irman.trade.validation;

import com.cgi.irman.trade.TradeDTO;
import com.cgi.irman.trade.exceptions.ValidatorException;

public interface ValidatorInterface {
    public void validate(TradeDTO trade) throws ValidatorException;
}
