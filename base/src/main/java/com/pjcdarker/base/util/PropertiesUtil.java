package com.pjcdarker.base.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author pjcdarker
 */
public class PropertiesUtil {

    private static ConcurrentMap<String, Properties> propertiesConcurrentMap = new ConcurrentHashMap<>();

    public static Properties getProperties(String propertiesPath) {
        return init(propertiesPath);
    }

    public static String get(String propertiesPath, String key) {
        Properties properties = init(propertiesPath);
        return String.valueOf(properties.get(key));
    }

    public static Map<String, String> toMap(String propertiesPath) {
        Map<String, String> propsMap = new HashMap<>();
        Properties properties = getProperties(propertiesPath);
        properties.forEach((K, V) -> propsMap.put((String) K, (String) V));
        return propsMap;
    }

    private static Properties init(String propertiesPath) {
        return propertiesConcurrentMap.computeIfAbsent(propertiesPath, PropertiesUtil::load);
    }

    private static Properties load(String propertiesPath) {
        Properties properties = new Properties();
        try {
            InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesPath);
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
