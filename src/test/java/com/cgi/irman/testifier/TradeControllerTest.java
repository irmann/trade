package com.cgi.irman.testifier;

import com.cgi.irman.testifier.Response;
import com.cgi.irman.testifier.Trade;
import com.cgi.irman.testifier.TradeService;
import com.cgi.irman.testifier.TradeController;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.SimpleDateFormat;

@WebMvcTest(TradeController.class)
@ContextConfiguration(classes={TestifierApplicationTests.class})
public class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService service;

    @Test
    public void tradeShouldReturnSuccessResponseFromService() throws Exception {
        Trade trade = new Trade();
        trade.setTradeId("T1");
        trade.setTradeVersion(1l);
        trade.setCountryPartyId("CP-1");
        trade.setBookId("B1");
        trade.setMaturityDate("20/05/2021");
        Mockito.when(service.store(trade)).thenReturn(new Response(0, "OK", 0));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/trade")).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("status:0")));
    }

}
