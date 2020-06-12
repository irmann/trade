package com.cgi.irman.testifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.orm.hibernate5.HibernateTemplate;

import java.util.Date;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SpringBootTest(classes = {TradeDao.class})
public class TradeDaoTest {

    @MockBean
    HibernateTemplate mockTemplate;

    @Autowired
    TradeDao tradeDao;


    @BeforeEach
    public void initMocks() {
        tradeDao.setHibernateTemplate(mockTemplate);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void storeTradeSuccessfully() throws Exception {
        TradeModel tradeModel = new TradeModel("T1", 1l, "CP-1", "B1", new Date(),
                new Date(), false);
        tradeDao.save(tradeModel);
        verify(mockTemplate, times(1)).save(tradeModel);
    }

}
