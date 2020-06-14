package com.cgi.irman.trade;

import com.cgi.irman.trade.validation.DateValidator;
import com.cgi.irman.trade.validation.NullValidator;
import com.cgi.irman.trade.validation.ValidatorInterface;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;

import static com.cgi.irman.trade.util.Constants.ERROR_VALIDATION_DATE;
import static com.cgi.irman.trade.util.Constants.ERROR_VALIDATION_REQUITED;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SpringBootTest(classes = {TradeService.class, TradeRepository.class})
@ContextConfiguration(classes={TestifierApplicationTests.class})
public class TradeServiceTest extends BaseTest{

    List<ValidatorInterface> validators;

    @MockBean
    HibernateTemplate mockTemplate;

    @MockBean
    TradeRepository tradeRepository;

    @Autowired
    TradeService tradeService;


    @BeforeEach
    public void initMocks() {
        validators = Arrays.asList(new NullValidator(), new DateValidator());
        tradeRepository.setHibernateTemplate(mockTemplate);
        tradeService.setTradeRepository(tradeRepository);
        tradeService.setValidators(validators);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void storeTradeSuccessfully() throws Exception {
        Trade trade = getTrade();
        TradeModel tradeModel = getTradeModel(trade);
        tradeService.store(trade);
        verify(tradeRepository, times(1)).save(tradeModel);
    }

    @Test
    public void failedByNullTradeId() throws Exception {
        Trade trade = getTrade();
        TradeModel tradeModel = getTradeModel(trade);
        trade.setTradeId(null);
        Response response = tradeService.store(trade);
        Assertions.assertEquals(1, response.getStatus());
        Assertions.assertEquals(ERROR_VALIDATION_REQUITED, response.getErrorCode());
    }

    @Test
    public void failedByWrongDateFormatTradeId() throws Exception {
        Trade trade = getTrade();
        TradeModel tradeModel = getTradeModel(trade);
        trade.setMaturityDate("a wrong format");
        Response response = tradeService.store(trade);
        Assertions.assertEquals(1, response.getStatus());
        Assertions.assertEquals(ERROR_VALIDATION_DATE, response.getErrorCode());
    }
}
