package com.cgi.irman.trade;

import com.cgi.irman.trade.util.Constants;
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

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    private Date getDate(String date) throws ParseException {
        return new SimpleDateFormat(Constants.DD_MM_YYYY).parse(date);
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

    //@Test
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

    //@Test
    public void findAllForMaxMaturityDate() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        TradeRepository tradeRepository = (TradeRepository) context.getBean("tradeRepository");
        TradeModel tradeModel1 = new TradeModel("T1", 1L, "CP-1", "B1", getDate("02/01/2020"),
                new Date(), false);
        TradeModel tradeModel2 = new TradeModel("T1", 2L, "CP-1", "B1", getDate("03/01/2020"),
                new Date(), true);
        TradeModel tradeModel3 = new TradeModel("T2", 1L, "CP-1", "B1", getDate("02/01/2021"),
                new Date(), false);
        TradeModel tradeModel4 = new TradeModel("T2", 2L, "CP-1", "B1", getDate("01/01/2021"),
                new Date(), true);
        tradeRepository.delete(tradeRepository.findAll());
        tradeRepository.save(tradeModel1);
        tradeRepository.save(tradeModel2);
        tradeRepository.save(tradeModel3);
        tradeRepository.save(tradeModel4);

        List<TradeModel> list = tradeRepository.findAllForMaxMaturityDate(new Date());
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals("T1", list.get(0).getTradeId());
        Assertions.assertEquals("2020-01-03 00:00:00.0", list.get(0).getMaturityDate().toString());
    }
}
