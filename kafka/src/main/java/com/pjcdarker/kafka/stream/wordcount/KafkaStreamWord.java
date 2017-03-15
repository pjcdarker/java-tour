package com.pjcdarker.kafka.stream.wordcount;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;

import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author pjc
 * @created 11/8/2016.
 */
public class KafkaStreamWord {

    private static final String KAFKA_STREAMS_CONFIG = "/kafka.streams.properties";

    public static void getCount() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-word-count");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.10.134:9092,192.168.10.134:9093,192.168.10.134:9094");
        properties.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, "192.168.10.134:2181");
        properties.put(StreamsConfig.CLIENT_ID_CONFIG, "1");
        properties.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        properties.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        // setting offset reset to earliest so that we can re-run the demo code with the same pre-loaded data
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KStreamBuilder builder = new KStreamBuilder();
        // input stream reads from a topic named "streams-input" the values of messages represent lines of text
        KStream<String, String> source = builder.stream("testTopic");

        KTable<String, Long> counts = source
                .flatMapValues(value -> Arrays.asList(value.toLowerCase(Locale.getDefault()).split(" ")))
                .map((key, value) -> new KeyValue<>(value, value))
                .groupByKey()
                .count("Counts");

        // need to override value serde to Long type
        System.out.println("===========KafkaStreamWord=========");
        counts.to(Serdes.String(), Serdes.Long(), "streams-output-01");

        KafkaStreams streams = new KafkaStreams(builder, properties);
        streams.start();

        while (true) {

        }

        // usually the stream application would be running forever,
        // in this example we just let it run for some time and stop since the input data is finite.
        // try {
        //     TimeUnit.SECONDS.sleep(5);
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }
        // streams.close();
    }

    public static void main(String[] args) {
        System.out.println(Serdes.String().getClass().getName());
    }

}
