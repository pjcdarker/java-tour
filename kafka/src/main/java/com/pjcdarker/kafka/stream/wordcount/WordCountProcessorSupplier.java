package com.pjcdarker.kafka.stream.wordcount;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorSupplier;

/**
 * @author pjc
 * @created 11/9/2016.
 */
public class WordCountProcessorSupplier implements ProcessorSupplier<String, String> {
    @Override
    public Processor get() {
        return WordCountProcessor.INSTANCE;
    }
}
