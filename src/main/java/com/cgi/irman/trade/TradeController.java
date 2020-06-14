package com.cgi.irman.trade;

import com.cgi.irman.trade.util.ApplicationContextHolder;
import com.cgi.irman.trade.util.JsonUtil;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static com.cgi.irman.trade.util.Constants.ERROR_INTERNAL;

@RestController
public class TradeController {

    Logger logger = LoggerFactory.getLogger(TradeController.class);

    KafkaTemplate<Integer, String> kafkaTemplate = createTemplate();

    //TODO Autowired is used only for an unit test. Unify initializing the service for unit test and production runtime
    @Autowired
    private TradeService service;

    @PostMapping("/api/v1/trade")
    public Response storeTrade(@RequestBody Trade trade) throws Exception {
        try{
            // To differentiate initializing the service between unit test and production runtime
            if(service.getValidators() == null)
                service = ApplicationContextHolder.getContext().getBean(TradeService.class);

            ExecutorService executor = Executors.newFixedThreadPool(2);
            executor.submit(() -> kafkaTemplate.send("test", JsonUtil.asJsonString(trade)));
            Future<Response> futureTask = executor.submit(() -> {
                return service.store(trade);
            });
            return futureTask.get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("error while storing a trade", e);
            return new Response(1, e.getMessage(), ERROR_INTERNAL);
        }
    }

    @GetMapping("/api/v1/trade")
    public Response findAllTrades() throws Exception {
        try{
            service = ApplicationContextHolder.getContext().getBean(TradeService.class);
            Response response = new Response(0, "OK", 0);
            return new Response(0, "OK", 0, service.findAll());
        } catch (Exception e) {
            logger.error("error while finding all trades", e);
            return new Response(1, e.getMessage(), ERROR_INTERNAL);
        }
    }

    //TODO move initializing of kafka template in applicationContext.xml
    private KafkaTemplate<Integer, String> createTemplate() {
        Map<String, Object> senderProps = senderProps();
        ProducerFactory<Integer, String> pf =
                new DefaultKafkaProducerFactory<>(senderProps);
        KafkaTemplate<Integer, String> template = new KafkaTemplate<>(pf);
        return template;
    }

    //TODO move to applicationContext.xml
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

