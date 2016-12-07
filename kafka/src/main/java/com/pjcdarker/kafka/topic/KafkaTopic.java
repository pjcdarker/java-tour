package com.pjcdarker.kafka.topic;

import com.pjcdarker.kafka.PropertiesUtil;
import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

import java.util.ArrayList;
import java.util.Properties;

/**
 * @author pjc
 * @created 11/4/2016.
 */
public class KafkaTopic {

    private static final String KAFKA_BROKER_CONFIG = "/kafka.broker.properties";
    private static final String KAFKA_TOPIC_CONFIG = "/kafka.topic.properties";

    private static Properties kafkaTopicProp;
    private static ZkUtils zkUtils;
    private static ZkClient zkClient;

    private static void init() {
        try {
            kafkaTopicProp = PropertiesUtil.load(KAFKA_TOPIC_CONFIG);
            String zookeeperHosts = kafkaTopicProp.getProperty("zookeeper.connect");
            int sessionTimeOutInMs = 10000;
            int connectionTimeOutInMs = 10000;
            zkClient = new ZkClient(zookeeperHosts, sessionTimeOutInMs, connectionTimeOutInMs, ZKStringSerializer$.MODULE$);
            zkUtils = new ZkUtils(zkClient, new ZkConnection(zookeeperHosts), false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void create() {
        try {
            if (zkUtils == null) {
                init();
            }
            String topicName = "topic8";
            if (AdminUtils.topicExists(zkUtils, topicName)) {
                deleteTopic(topicName);
            }
            int partitions = 2;
            int replication = 3;
            AdminUtils.createTopic(zkUtils, topicName, partitions, replication, new Properties(), RackAwareMode.Safe$.MODULE$);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (zkClient != null) {
                zkClient.close();
            }
        }
    }

    public static ArrayList<String> getTopicNames() {
        Properties kafkaTopicConfig = PropertiesUtil.load(KAFKA_TOPIC_CONFIG);
        String topicName = (String) kafkaTopicConfig.get("topic.name");
        String[] topicNames = topicName.split(",");
        ArrayList<String> topics = new ArrayList<>();
        for (String name : topicNames) {
            topics.add(name);
        }
        return topics;
    }

    public static void deleteTopic(String topicName) {
        if (zkUtils == null) {
            init();
        }
        AdminUtils.deleteTopic(zkUtils, topicName);
    }
}
