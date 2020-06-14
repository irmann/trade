package com.cgi.irman.trade.validation;

import com.cgi.irman.trade.Trade;
import com.cgi.irman.trade.exceptions.ValidatorException;
import com.cgi.irman.trade.util.Constants;

import java.text.MessageFormat;
import java.util.regex.Pattern;

import static com.cgi.irman.trade.util.Constants.ERROR_VALIDATION_DATE;

public class DateValidator extends ValidatorBase implements ValidatorInterface {
    private static Pattern DATE_PATTERN = Pattern.compile(
            "^\\d{2}/\\d{2}/\\d{4}$");

    @Override
    public void validate(Trade trade) throws ValidatorException {
        if (!DATE_PATTERN.matcher(trade.getMaturityDate()).matches()) {
            throwException("Maturity date");
        }else{
            if (trade.getMaturityDate() != null  && !DATE_PATTERN.matcher(trade.getMaturityDate()).matches()) {
                throwException("Created date");
            }
        }
    }

    private void throwException(String name) throws ValidatorException {
        throw new ValidatorException(ERROR_VALIDATION_DATE,
                MessageFormat.format("{0} format is invalid. expected pattern is {0}"
                        ,name, Constants.DD_MM_YYYY));
    }
}
