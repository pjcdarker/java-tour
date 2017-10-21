package com.pjcdarker.kafka.stream;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorSupplier;

/**
 * @author pjc
 * @created 11/9/2016.
 */
public class WordCountProcessorSupplier implements ProcessorSupplier<String, Processor> {
    @Override
    public Processor get() {
        return WordCountProcessor.INSTANCE;
    }
}
