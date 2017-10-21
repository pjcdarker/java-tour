package com.pjcdarker.kafka.quickstart;

import com.pjcdarker.kafka.Kafkas;
import org.apache.kafka.clients.consumer.Consumer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author pjc
 * @created 10/27/2016.
 */
public class SimpleConsumerTest {

    private static List<String> topics;
    private static Consumer kafkaConsumer;

    @BeforeAll
    public static void before() {
        topics = Arrays.asList("topic01", "topic02");
        kafkaConsumer = Kafkas.getConsumer();
    }

    @Test
    public void test() {
        SimpleConsumer simpleConsumerTask = new SimpleConsumer(topics, kafkaConsumer);
        Thread consumerThread = new Thread(simpleConsumerTask);
        consumerThread.start();

        // kafkaConsumer.wakeup();
        // System.out.println("Stopping consumer .....");
        // try {
        //     consumerThread.join(60000);
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }
    }
}
