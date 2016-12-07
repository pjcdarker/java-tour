package com.pjcdarker.kafka.topic;

import org.junit.Test;

/**
 * @author pjc
 * @created 11/8/2016.
 */
public class TestKafkaTopic {

    @Test
    public void testCreate() {
        KafkaTopic.create();
        KafkaTopic.getTopicNames().forEach(System.out::println);
    }
}
