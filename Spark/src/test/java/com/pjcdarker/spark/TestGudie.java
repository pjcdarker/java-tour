package com.pjcdarker.spark;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author pjc
 * @created 11/6/2016.
 */
public class TestGudie {

    @BeforeClass
    public static void before() {

    }

    @Test
    public void textCount() {
        String path = "src/main/resources/spark.md";
        Gudie.textFileCount(path);
    }

    @Test
    public void parallelize() {
        Gudie.parallelize();
    }
}
