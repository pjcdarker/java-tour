package com.pjcdarker.kafka.streams;

import com.pjcdarker.kafka.quickstart.SimpleConsumer;
import com.pjcdarker.kafka.stream.wordcount.KafkaStreamWord;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author pjc
 * @created 11/9/2016.
 */
public class TestKafkaStreamWord {

    @Test
    public void testCount() {
        KafkaStreamWord.getCount();
    }
}
