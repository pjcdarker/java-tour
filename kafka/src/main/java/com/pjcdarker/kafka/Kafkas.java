package com.pjcdarker.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import java.util.Properties;

/**
 * @author pjc
 * @create 10/20/2016
 */
public class Kafkas {

    private static final String KAFKA_PRODUCER_CONFIG = "/kafka.producer.properties";
    private static final String KAFKA_CONSUMER_CONFIG = "/kafka.consumer.properties";

    public static Producer getProducer() {
        Properties configProperties = PropertiesUtil.load(KAFKA_PRODUCER_CONFIG);
        return getProducer(configProperties);
    }

    public static Producer getProducer(String configPath) {
        Properties configProperties = PropertiesUtil.load(configPath);
        return getProducer(configProperties);
    }

    public static Producer getProducer(Properties configProperties) {
        Producer producer = new KafkaProducer(configProperties);
        return producer;
    }

    public static Consumer getConsumer() {
        Properties configProperties = PropertiesUtil.load(KAFKA_CONSUMER_CONFIG);
        return getConsumer(configProperties);
    }

    public static Consumer getConsumer(String configPath) {
        Properties configProperties = PropertiesUtil.load(configPath);
        return getConsumer(configProperties);
    }

    public static Consumer getConsumer(Properties configProperties) {
        Consumer kafkaConsumer = new KafkaConsumer<>(configProperties);
        return kafkaConsumer;
    }
}
