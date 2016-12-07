package com.pjcdarker.kafka.quickstart;

import com.pjcdarker.kafka.Kafkas;
import com.pjcdarker.kafka.partition.ADPartitioner;
import com.pjcdarker.kafka.topic.KafkaTopic;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * @author pjc
 * @create 10/20/2016
 */
public class SimpleProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleProducer.class);

    private List<String> topics;
    private Producer producer;

    public SimpleProducer(List topics, Producer producer) {
        this.topics = topics;
        this.producer = producer;
    }

    public void send(String message) {
        System.out.println("producer send msg: " + message);
        this.topics.parallelStream().forEach(topic -> {
            ProducerRecord producerRecord = new ProducerRecord(topic, topic + message, message);
            try {
                RecordMetadata recordMetadata = (RecordMetadata) producer.send(producerRecord).get();
                LOGGER.info("{send} msg -> topic -> partition -> offset :  " + message + " -> " + topic + " -> " + recordMetadata.partition() + " -> " + recordMetadata.offset());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    public void sendAndCallback(String message) {
        System.out.println("producer send msg: " + message);
        this.topics.parallelStream().forEach(topic -> {
            ProducerRecord producerRecord = new ProducerRecord(topic, topic + "-key", message);
            producer.send(producerRecord, (metadata, ex) -> {
                Optional.ofNullable(metadata).ifPresent(recordMetadata -> {
                    LOGGER.info("{sendAndCallback} msg -> topic -> partition -> offset :  " + message + " -> " + topic + " -> " + recordMetadata.partition() + " -> " + recordMetadata.offset());
                });

                Optional.ofNullable(ex).ifPresent(e -> {
                    LOGGER.error("kafka consumer execption" + e.getMessage());
                    e.printStackTrace();
                });
            });
        });
    }

    public void close() {
        producer.flush();
        producer.close();
    }

    public static void main(String[] args) {
        List<String> topics = KafkaTopic.getTopicNames();
        Producer producer = Kafkas.getProducerInstance();
        SimpleProducer simpleProducer = new SimpleProducer(topics, producer);

        String name = ADPartitioner.class.getCanonicalName();
        System.out.println(name);

        Scanner scanner = new Scanner(System.in);
        String msg = "";
        while (!msg.equals("-1")) {
            msg = scanner.nextLine();
            simpleProducer.send(msg);
            // simpleProducer.sendAndCallback(msg);
        }
        scanner.close();
        simpleProducer.close();
    }
}
