package com.cgi.irman.testifier;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
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

    @Test
    public void findMaxVersion() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        TradeDao tradeDao = (TradeDao) context.getBean("tradeDao");
        TradeModel tradeModel = new TradeModel("T1", 1l, "CP-1", "B1", new Date(),
                new Date(), false);
        tradeDao.delete(tradeDao.findByTradeId(tradeModel.getTradeId()));
        tradeDao.save(tradeModel);
        Assertions.assertEquals(tradeModel.getTradeVersion(), tradeDao.findMaxVersion(tradeModel.getTradeId()).get());
        TradeModel tradeModelVersion2 = new TradeModel("T1", 2l, "CP-1", "B1", new Date(),
                new Date(), true);
        tradeDao.save(tradeModelVersion2);
        Assertions.assertEquals(tradeModelVersion2.getTradeVersion(), tradeDao.findMaxVersion(tradeModel.getTradeId()).get());
        TradeModel tradeMode3 = new TradeModel("T3", 1l, "CP-1", "B1", new Date(),
                new Date(), false);
        tradeDao.save(tradeMode3);
        Assertions.assertEquals(tradeMode3.getTradeVersion(), tradeDao.findMaxVersion(tradeMode3.getTradeId()).get());
    }

}
