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
    private static final String KAFKA_COMSUMER_CONFIG = "/kafka.consumer.properties";

    public static Producer getProducerInstance() {
        Properties configProperties = PropertiesUtil.load(KAFKA_PRODUCER_CONFIG);
        return getProducerInstance(configProperties);
    }

    public static Producer getProducerInstance(String configPath) {
        Properties configProperties = PropertiesUtil.load(configPath);
        return getProducerInstance(configProperties);
    }

    public static Producer getProducerInstance(Properties configProperties) {
        Producer producer = new KafkaProducer(configProperties);
        return producer;
    }

    public static Consumer getConsumerInstance() {
        Properties configProperties = PropertiesUtil.load(KAFKA_COMSUMER_CONFIG);
        return getConsumerInstance(configProperties);
    }

    public static Consumer getConsumerInstance(String configPath) {
        Properties configProperties = PropertiesUtil.load(configPath);
        return getConsumerInstance(configProperties);
    }

    public static Consumer getConsumerInstance(Properties configProperties) {
        Consumer kafkaConsumer = new KafkaConsumer<>(configProperties);
        return kafkaConsumer;
    }
}
