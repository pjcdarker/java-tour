package com.pjcdarker.learn;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author pjc
 * @create 2016-10-09
 */
public class PropertiesUtil {

    private static ConcurrentMap<String, Properties> propertiesConcurrentMap = new ConcurrentHashMap<>();

    public static Properties getProperties(String propertiesPath) {
        return init(propertiesPath);
    }

    public static String get(String propertiesPath, String key) {
        Properties properties = init(propertiesPath);
        String value = (String) properties.get(key);
        return value;
    }

    public static Map<String, String> toMap(String propertiesPath) {
        Map<String, String> propsMap = new HashMap<>();
        Properties properties = getProperties(propertiesPath);
        properties.forEach((K, V) -> propsMap.put((String) K, (String) V));
        return propsMap;
    }

    public static String toJsonString(String propertiesPath) {
        Properties properties = getProperties(propertiesPath);
        return properties.entrySet()
                         .stream()
                         .map(props -> props.getKey() + ":" + props.getValue())
                         .collect(Collectors.joining(",", "{", "}"));
    }

    private static Properties init(String propertiesPath) {
        return propertiesConcurrentMap.computeIfAbsent(propertiesPath, k -> load(k));
    }

    private static Properties load(String propertiesPath) {
        Properties properties = new Properties();
        try {
            InputStream inputStream = PropertiesUtil.class.getResourceAsStream(propertiesPath);
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
