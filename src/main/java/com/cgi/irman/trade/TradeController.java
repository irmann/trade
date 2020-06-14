package com.cgi.irman.trade;

import com.cgi.irman.trade.util.ApplicationContextHolder;
import com.cgi.irman.trade.util.JsonUtil;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.cgi.irman.trade.util.Constants.ERROR_INTERNAL;
import static com.cgi.irman.trade.util.JsonUtil.asTrade;

@RestController
public class TradeController {
    public TradeController() {
        ContainerProperties containerProps = new ContainerProperties("test");
        containerProps.setMessageListener(new MessageListener<Integer, String>() {
            @Override
            public void onMessage(ConsumerRecord<Integer, String> message) {
                logger.info("received: " + message);
                handleMessage(message.value());
            }
        });
        KafkaMessageListenerContainer<Integer, String> container = createContainer(containerProps);
        container.start();
    }

    Logger logger = LoggerFactory.getLogger(TradeController.class);

    KafkaTemplate<Integer, String> kafkaTemplate = createTemplate();

    //TODO Autowired is used only for an unit test. Unify initializing the service for unit test and production runtime
    @Autowired
    private TradeService service;

    @PostMapping("/api/v1/trade")
    public Response storeTrade(@RequestBody Trade trade) {
        try{
            //To differentiate initializing the service between unit test and production runtime
            if(service.getValidators() == null)
                service = ApplicationContextHolder.getContext().getBean(TradeService.class);

            ExecutorService executor = Executors.newFixedThreadPool(2);
            executor.submit(() -> kafkaTemplate.send("test2", JsonUtil.asJsonString(trade)));
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
    public Response findAllTrades() {
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

    //TODO move to applicationContext.xml
    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "foo");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    private KafkaMessageListenerContainer<Integer, String> createContainer(
            ContainerProperties containerProps) {
        Map<String, Object> props = consumerProps();
        DefaultKafkaConsumerFactory<Integer, String> cf =
                new DefaultKafkaConsumerFactory<Integer, String>(props);
        KafkaMessageListenerContainer<Integer, String> container =
                new KafkaMessageListenerContainer<>(cf, containerProps);
        return container;
    }

    private void handleMessage(String json) {
        try {
            Trade trade = asTrade(json);
            service = ApplicationContextHolder.getContext().getBean(TradeService.class);
            service.store(trade);
        }catch (Exception e){
            logger.error("error while handling a message from Kafka");
        }
    }
}

