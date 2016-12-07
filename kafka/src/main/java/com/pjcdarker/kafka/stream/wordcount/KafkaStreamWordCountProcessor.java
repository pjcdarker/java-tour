package com.pjcdarker.kafka.stream.wordcount;

import com.pjcdarker.kafka.PropertiesUtil;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.processor.TopologyBuilder;
import org.apache.kafka.streams.state.Stores;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author pjc
 * @created 11/9/2016.
 */
public class KafkaStreamWordCountProcessor {

    private static final String KAFKA_STREAMS_CONFIG = "/kafka.streams.properties";

    public static void processe() {
        TopologyBuilder builder = new TopologyBuilder();

        // 添加数据源 由streams-file-input topic 提供消息
        builder.addSource("Source", "streams-file-input");

        // 添加 Process 是 Source的子节点
        builder.addProcessor("Process", new WordCountProcessorSupplier(), "Source");

        // create state 与 Processor 链接
        builder.addStateStore(Stores.create("Counts").withStringKeys().withIntegerValues().inMemory().build(), "Process");

        // 添加 sink 节点到 TopologyBuilder ，从不同的Process节点输出到 streams-wordcount-processor-output topic
        builder.addSink("Sink", "streams-wordcount-processor-output", "Process");

        Properties properties = PropertiesUtil.load(KAFKA_STREAMS_CONFIG);
        KafkaStreams streams = new KafkaStreams(builder, properties);
        streams.start();

        // usually the stream application would be running forever,
        // in this example we just let it run for some time and stop since the input data is finite.
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        streams.close();
    }
}
