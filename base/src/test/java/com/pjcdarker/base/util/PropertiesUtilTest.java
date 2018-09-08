package com.pjcdarker.base.util;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author pjc
 * @create 2016-10-09
 */
public class PropertiesUtilTest {

    private static final String PROPERTIES_PATH = "app.properties";

    @Test
    public void testGet() {
        String username = PropertiesUtil.get(PROPERTIES_PATH, "username");
        System.out.println("username: " + username);
    }

    @Test
    public void testToMap() {
        Map<String, String> map = PropertiesUtil.toMap(PROPERTIES_PATH);
        System.out.println("map: " + map);
    }

    @Test
    public void partten() {
        Pattern pattern = Pattern.compile("[\\w\\W\\u4e00-\\u9fa5]");
        Matcher matcher = pattern.matcher("你好abc123@!`");
        while (matcher.find()) {
            String result = matcher.group();
            System.out.println(result);
        }
    }
}
