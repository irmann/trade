package com.cgi.irman.testifier;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.*;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GreetingController {

    @Autowired

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    KafkaTemplate<Integer, String> kafkaTemplate = createTemplate();

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }



    @GetMapping("/greetingtokafka")
    public String greetingToKafka(@RequestParam(value = "name", defaultValue = "World") String name) throws InterruptedException, ExecutionException, TimeoutException {
        String content =  String.format(template, name);
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        GreetingDao greetingDao = (GreetingDao) context.getBean("greetingDao");
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> greetingDao.saveGreeting(new Greeting(counter.incrementAndGet(), content)));
        Future<Integer> futureTask = executor.submit(() -> {
            kafkaTemplate.send("test", content);
            return content.length();
        });
        Integer result = futureTask.get(5, TimeUnit.SECONDS);
        System.out.println(String.format("%s characters has been sent to Kafka", result) );
        return "please see your kafka consumer console and postgres";
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