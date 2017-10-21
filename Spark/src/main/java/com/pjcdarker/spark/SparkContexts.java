package com.pjcdarker.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * @author pjcdarker
 * @created 2017-02-28.
 */
public class SparkContexts {

    static JavaSparkContext local(String appName) {
        return getContext("local[*]", appName);
    }

    private static JavaSparkContext getContext(String master, String appName) {
        SparkConf sparkConf = new SparkConf().setMaster(master).setAppName(appName);
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
        return sparkContext;
    }
}
