package com.cgi.irman.trade;

import com.cgi.irman.trade.util.ApplicationContextHolder;
import com.cgi.irman.trade.util.JsonUtil;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@RestController
public class TradeController {

    KafkaTemplate<Integer, String> kafkaTemplate = createTemplate();

    public TradeService getService() {
        return service;
    }
    @Autowired
    public void setService(TradeService service) {
        this.service = service;
    }

    private TradeService service;

    @PostMapping("/api/v1/trade")
    public Response storeTrade(@RequestBody Trade trade) throws Exception {
        if(service.getValidators() == null)
            service = ApplicationContextHolder.getContext().getBean(TradeService.class);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> kafkaTemplate.send("test", JsonUtil.asJsonString(trade)));
        Future<Response> futureTask = executor.submit(() -> {
            return service.store(trade);
        });
        Response response = futureTask.get(5, TimeUnit.SECONDS);
        return response;
    }

    @GetMapping("/api/v1/trade")
    public Response findAllTrades() throws Exception {
        service = ApplicationContextHolder.getContext().getBean(TradeService.class);
        Response response = new Response(0, "OK", 0);
        response.setPayload(service.findAll());
        return response;
    }

    private KafkaTemplate<Integer, String> createTemplate() {
        Map<String, Object> senderProps = senderProps();
        ProducerFactory<Integer, String> pf =
                new DefaultKafkaProducerFactory<>(senderProps);
        KafkaTemplate<Integer, String> template = new KafkaTemplate<>(pf);
        return template;
    }

    private Map<String, Object> senderProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

}

