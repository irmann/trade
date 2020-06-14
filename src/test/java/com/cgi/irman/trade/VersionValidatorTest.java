package com.cgi.irman.trade;

import com.cgi.irman.trade.exceptions.ValidatorException;
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


@SpringBootTest(classes = {VersionValidator.class})
public class VersionValidatorTest extends BaseTest{

    @MockBean
    HibernateTemplate mockTemplate;

    @MockBean
    TradeRepository tradeDao;

    @Autowired
    VersionValidator versionValidator;

    @BeforeEach
    public void initMocks() {
        tradeDao.setHibernateTemplate(mockTemplate);
        versionValidator.setTradeDao(tradeDao);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void failedValidateVersion() throws Exception {
        Trade trade = getTrade();
        trade.setTradeVersion(1L);
        Optional<Long> max = Optional.of(2L);
        when(tradeDao.findMaxVersion(trade.getTradeId())).thenReturn(max);
        try{
            versionValidator.validate(trade);
            Assertions.fail();
        }catch (ValidatorException validatorException){
            Assertions.assertEquals(ERROR_VALIDATION_VERSION ,validatorException.getCode());
        }
    }

    @Test
    public void successfulValidateVersion() throws Exception {
        Trade trade = getTrade();
        trade.setTradeVersion(2L);
        Optional<Long> max = Optional.of(1L);
        when(tradeDao.findMaxVersion(trade.getTradeId())).thenReturn(max);
        try{
            versionValidator.validate(trade);
        }catch (ValidatorException validatorException){
            Assertions.fail();
        }
    }

}
