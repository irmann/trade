package com.cgi.irman.testifier.validation;

import com.cgi.irman.testifier.Trade;
import com.cgi.irman.testifier.exceptions.ValidatorException;

public interface ValidatorInterface {
    public void validate(Trade trade) throws ValidatorException;
}
