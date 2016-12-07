package com.pjcdarker.kafka.quickstart;

import com.pjcdarker.kafka.Kafkas;
import org.apache.kafka.clients.consumer.Consumer;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author pjc
 * @created 10/27/2016.
 */
public class SimpleConsumerTest {

    private static List<String> topics;
    private static Consumer kafkaConsumer;

    @BeforeClass
    public static void before() {
        topics = Arrays.asList("topic01", "topic02");
        kafkaConsumer = Kafkas.getConsumerInstance();
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
