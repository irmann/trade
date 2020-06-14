package com.cgi.irman.trade;

import com.cgi.irman.trade.exceptions.ValidatorException;
import com.cgi.irman.trade.validation.ExpiredValidator;
import com.cgi.irman.trade.validation.VersionValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.orm.hibernate5.HibernateTemplate;

import java.util.Optional;

import static com.cgi.irman.trade.util.Constants.ERROR_VALIDATION_VERSION;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = {ExpiredValidator.class})
public class ExpiredValidatorTest extends BaseTest{

    @MockBean
    HibernateTemplate mockTemplate;

    @MockBean
    TradeRepository tradeRepository;

    @Autowired
    ExpiredValidator expiredValidator;

    @BeforeEach
    public void initMocks() {
        tradeRepository.setHibernateTemplate(mockTemplate);
        expiredValidator.setTradeRepository(tradeRepository);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void failedValidateExpired() throws Exception {
        Trade trade = getTrade();
        TradeModel tradeModel = getTradeModel(trade);
        tradeModel.setExpired(true);
        when(tradeRepository.findLastVersionByTradId(trade.getTradeId())).thenReturn(Optional.of(tradeModel));
        try{
            expiredValidator.validate(trade);
            Assertions.fail();
        }catch (ValidatorException validatorException){
            Assertions.assertEquals(ERROR_VALIDATION_VERSION ,validatorException.getCode());
        }
    }

}
