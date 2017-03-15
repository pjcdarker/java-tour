package com.pjcdarker.kafka.partition;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author pjc
 * @created 10/25/2016.
 */
public class ADPartitioner implements Partitioner {

    private final Logger logger = LoggerFactory.getLogger(ADPartitioner.class);
    private static Map<String, Integer> adPartitionMap = new ConcurrentHashMap<>();

    /**
     * 每个消息调用一次
     *
     * @param topic
     * @param key
     * @param keyBytes
     * @param value
     * @param valueBytes
     * @param cluster
     * @return
     */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        logger.info("topic: " + topic + ", value: " + value);
        List<PartitionInfo> partitions = cluster.availablePartitionsForTopic(topic);
        if (adPartitionMap.containsKey(value)) {
            return adPartitionMap.get(value);
        } else {
            int partitionTopicSize = cluster.topics().size();
            int partition = value.hashCode() % partitionTopicSize;
            partition = partition % adPartitionMap.size();
            logger.info("partition: " + partition);
            return 0;
        }
    }

    /**
     * 启动执行一次
     *
     * @param configs
     */
    @Override
    public void configure(Map<String, ?> configs) {
        Objects.requireNonNull(configs, "configs is not null");
        logger.info("ADPartitioner.configure " + configs);
        configs.entrySet().forEach(entry -> {
            String key = entry.getKey();
            logger.info("key: " + key);
            if (key.startsWith("partitions.")) {
                String value = (String) entry.getValue();
                int partitionId = Integer.parseInt(key.substring("partitions.".length()));
                adPartitionMap.put(value, partitionId);
            }
        });
    }

    @Override
    public void close() {
        adPartitionMap.clear();
    }
}
