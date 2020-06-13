package com.cgi.irman.testifier;

import com.cgi.irman.testifier.util.JsonUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest( controllers =  TradeController.class)
public class TradeControllerTest extends BaseTest{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService service;

    @Test
    public void tradeShouldReturnSuccessResponseFromService() throws Exception {
        Trade trade = getTrade();
        Response response = new Response(0, "OK", 0);
        Mockito.when(service.store(any(Trade.class))).thenReturn(response);
        this.mockMvc.perform(post("/trade")
                .content(JsonUtil.asJsonString(trade))
                .contentType("application/json")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(
                        JsonUtil.asJsonString(response))));
    }
}
