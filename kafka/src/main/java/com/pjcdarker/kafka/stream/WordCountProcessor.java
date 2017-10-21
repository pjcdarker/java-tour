package com.pjcdarker.kafka.stream;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Locale;

/**
 * @author pjc
 * @date 11/24/2016.
 */
public class WordCountProcessor implements Processor {

    private static final String STATE_STORE_KEY = "Counts";
    public static final WordCountProcessor INSTANCE = new WordCountProcessor();

    private ProcessorContext context;
    private KeyValueStore<String, Integer> kvStore;

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
        this.context.schedule(1000);
        this.kvStore = (KeyValueStore<String, Integer>) context.getStateStore(STATE_STORE_KEY);
    }

    /**
     * 执行接收的消息,并根据时间周期执行 punctuate()
     *
     * @param key
     * @param value
     */
    @Override
    public void process(Object key, Object value) {
        String[] words = ((String) value).toLowerCase(Locale.getDefault()).split(" ");
        for (String word : words) {
            Integer oldValue = this.kvStore.get(word);
            if (oldValue == null) {
                this.kvStore.put(word, 1);
            } else {
                this.kvStore.put(word, oldValue + 1);
            }
        }
        context.commit();
    }

    @Override
    public void punctuate(long timestamp) {
        try (KeyValueIterator<String, Integer> keyValueIterator = this.kvStore.all()) {
            System.out.println("----------- " + timestamp + " ----------- ");
            while (keyValueIterator.hasNext()) {
                KeyValue<String, Integer> entry = keyValueIterator.next();
                System.out.println("[" + entry.key + ", " + entry.value + "]");
                context.forward(entry.key, entry.value.toString());
            }
        }
    }

    @Override
    public void close() {
        this.kvStore.close();
    }
}
