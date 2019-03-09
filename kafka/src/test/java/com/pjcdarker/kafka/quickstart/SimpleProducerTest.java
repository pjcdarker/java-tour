package com.pjcdarker.kafka.quickstart;

import com.pjcdarker.kafka.KafkaProps;
import org.apache.kafka.clients.producer.Producer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author pjc
 * @create 10/20/2016
 */
public class SimpleProducerTest {

    private static List<String> topics;
    private static Producer producer;
    private static SimpleProducer simpleProducer;

    @BeforeAll
    public static void before() {
        topics = Arrays.asList("topic01", "topic02");
        producer = KafkaProps.getProducer();
        simpleProducer = new SimpleProducer(topics, producer);
    }

    @Test
    public void test() {
        simpleProducer.send("nihao");
        simpleProducer.close();
    }
}
