package com.pjcdarker.kafka;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author pjc
 * @created 10/25/2016.
 */
public class PropertiesUtil {

    private static final String KAFKA_PRODUCER_CONFIG = "/kafka.producer.properties";
    private static final String KAFKA_COMSUMER_CONFIG = "/kafka.consumer.properties";

    public static Properties load(String configPath) {
        return init(configPath);
    }

    private static Properties init(String configPath) {
        Properties properties = new Properties();
        try {
            InputStream inputStream = PropertiesUtil.class.getResourceAsStream(configPath);
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
