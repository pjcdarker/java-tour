package com.pjcdarker.kafka.quickstart;

import com.pjcdarker.kafka.Kafkas;
import com.pjcdarker.kafka.topic.KafkaTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 * @author pjc
 * @create 10/20/2016
 */
public class SimpleConsumer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleConsumer.class);

    private List<String> topics;
    private Consumer consumer;
    private boolean isRebalanceListener;

    public SimpleConsumer(List topics, Consumer consumer) {
        this.topics = topics;
        this.consumer = consumer;
    }

    void setRebalanceListener(boolean isRebalanceListener) {
        this.isRebalanceListener = isRebalanceListener;
    }

    @Override
    public void run() {
        if (isRebalanceListener) {
            consumer.subscribe(topics, new ConsumerRebalanceListener() {
                public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                    LOGGER.info("{} topic-partitions are revoked from this consumer\n", Arrays.toString(partitions.toArray()));
                }

                public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                    LOGGER.info("{} topic-partitions are assigned to this consumer\n", Arrays.toString(partitions.toArray()));
                }
            });
        } else {
            consumer.subscribe(topics);
        }
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    String key = record.key();
                    String value = record.value();
                    String topic = record.topic();
                    System.out.println(topic + "--> " + key + " --> " + value);
                }
            }
        } catch (WakeupException ex) {
            ex.printStackTrace();
            LOGGER.error("Exception caught " + ex.getMessage());
        } finally {
            consumer.close();
            LOGGER.info("After closing KafkaConsumer");
        }
    }


    public static void main(String[] args) throws InterruptedException {
        List<String> topics = KafkaTopic.getTopicNames();
        topics.add("streams-output-01");
        Consumer kafkaConsumer = Kafkas.getConsumer();

        SimpleConsumer simpleConsumerTask = new SimpleConsumer(topics, kafkaConsumer);
        simpleConsumerTask.setRebalanceListener(true);

        Thread consumerThread = new Thread(simpleConsumerTask);
        consumerThread.start();
        Scanner scanner = new Scanner(System.in);
        String cmd = "";
        while (!cmd.equals("-1")) {
            cmd = scanner.nextLine();
        }
        scanner.close();
        kafkaConsumer.wakeup();
        LOGGER.info("Stopping consumer .....");
        consumerThread.join(60000);
    }
}
