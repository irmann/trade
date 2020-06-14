package com.cgi.irman.trade;

import com.cgi.irman.trade.exceptions.ValidatorException;
import com.cgi.irman.trade.util.Constants;
import com.cgi.irman.trade.validation.ValidatorInterface;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class TradeService {

    List<ValidatorInterface> validators;
    TradeRepository tradeDao;

    public Response store(Trade trade) throws Exception {
        for (ValidatorInterface v : validators) {
            try {
                v.validate(trade);
            } catch (ValidatorException validatorException) {
                return new Response(1, validatorException.getMessage(), validatorException.getCode());
            }
        }
        TradeModel tradeModel = new TradeModel(trade.getTradeId(), trade.getTradeVersion(),
                trade.getCountryPartyId(), trade.getBookId(),
                getDate(trade.getMaturityDate()),
                getCreatedDate(trade), trade.getExpired());
        tradeDao.save(tradeModel);
        return new Response(0, "OK", 0);
    }

    private Date getCreatedDate(Trade trade) throws ParseException {
        return trade.getCreatedDate() != null ? getDate(trade.getCreatedDate()) : new Date();
    }

    private Date getDate(String date) throws ParseException {
        return new SimpleDateFormat(Constants.DD_MM_YYYY).parse(date);
    }

    public List<TradeModel> findAll() {
        return tradeDao.findAll();
    }
    public TradeRepository getTradeDao() {
        return tradeDao;
    }

    public void setTradeDao(TradeRepository tradeDao) {
        this.tradeDao = tradeDao;
    }

    public List<ValidatorInterface> getValidators() {
        return validators;
    }

    public void setValidators(List<ValidatorInterface> validators) {
        this.validators = validators;
    }
}
