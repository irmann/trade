package com.cgi.irman.testifier.validation;

import com.cgi.irman.testifier.Trade;
import com.cgi.irman.testifier.exceptions.ValidatorException;
import static com.cgi.irman.testifier.util.Constants.ERROR_VALIDATION_REQUITED;

public class NullValidator  extends ValidatorBase implements ValidatorInterface{
    @Override
    public void validate(Trade trade) throws ValidatorException {
        if (trade.getTradeId() == null)
            throw createException( "Trade id is required");
        if (trade.getTradeVersion() == null)
            throw createException("Version is required");
        if (trade.getCountryPartyId() == null)
            throw createException("Country party id is required");
        if (trade.getBookId() == null)
            throw createException("Book id is required");
        if (trade.getMaturityDate() == null)
            throw createException("Maturity date is required");
    }

    private ValidatorException createException(String msg) {
        return new ValidatorException(ERROR_VALIDATION_REQUITED , msg);
    }
}
