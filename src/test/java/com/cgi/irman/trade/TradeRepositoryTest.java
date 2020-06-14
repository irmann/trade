package com.cgi.irman.trade;

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


@SpringBootTest(classes = {TradeRepository.class})
public class TradeRepositoryTest {

    @MockBean
    HibernateTemplate mockTemplate;

    @Autowired
    TradeRepository tradeRepository;


    @BeforeEach
    public void initMocks() {
        tradeRepository.setHibernateTemplate(mockTemplate);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void storeTradeSuccessfully() throws Exception {
        TradeModel tradeModel = new TradeModel("T1", 1L, "CP-1", "B1", new Date(),
                new Date(), false);
        tradeRepository.save(tradeModel);
        verify(mockTemplate, times(1)).save(tradeModel);
    }

    // an integration test with DB
    //@Test
    public void findMaxVersion() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        TradeRepository tradeRepository = (TradeRepository) context.getBean("tradeRepository");
        TradeModel tradeModel = new TradeModel("T1", 1L, "CP-1", "B1", new Date(),
                new Date(), false);
        tradeRepository.delete(tradeRepository.findByTradeId(tradeModel.getTradeId()));
        tradeRepository.save(tradeModel);
        Assertions.assertEquals(tradeModel.getTradeVersion(), tradeRepository.findMaxVersion(tradeModel.getTradeId()).get());
        TradeModel tradeModelVersion2 = new TradeModel("T1", 2L, "CP-1", "B1", new Date(),
                new Date(), true);
        tradeRepository.save(tradeModelVersion2);
        Assertions.assertEquals(tradeModelVersion2.getTradeVersion(), tradeRepository.findMaxVersion(tradeModel.getTradeId()).get());
        TradeModel tradeMode3 = new TradeModel("T3", 1L, "CP-1", "B1", new Date(),
                new Date(), false);
        tradeRepository.save(tradeMode3);
        Assertions.assertEquals(tradeMode3.getTradeVersion(), tradeRepository.findMaxVersion(tradeMode3.getTradeId()).get());
    }

    @Test
    public void findLastVersion() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        TradeRepository tradeRepository = (TradeRepository) context.getBean("tradeRepository");
        TradeModel tradeModel = new TradeModel("T1", 1L, "CP-1", "B1", new Date(),
                new Date(), false);
        TradeModel tradeModelVersion2 = new TradeModel("T1", 2L, "CP-1", "B1", new Date(),
                new Date(), true);
        tradeRepository.delete(tradeRepository.findByTradeId(tradeModel.getTradeId()));
        tradeRepository.save(tradeModel);
        tradeRepository.save(tradeModelVersion2);
        Assertions.assertEquals(tradeModelVersion2.getTradeVersion(), tradeRepository.findLastVersionByTradId(tradeModel
                .getTradeId()).get().getTradeVersion());
    }

}
