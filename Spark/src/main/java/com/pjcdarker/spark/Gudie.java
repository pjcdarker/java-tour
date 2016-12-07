package com.pjcdarker.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * @author pjc
 * @create 10/20/2016
 */
public class Gudie {

    public static void textFileCount(String path) {
        // local 本地模式
        SparkConf sparkConf = new SparkConf().setMaster("local[*]").setAppName("GudieApp");

        // tells Spark how to access a cluster
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
        JavaRDD<String> javaRDD = javaSparkContext.textFile(path).cache();

        long numA = javaRDD.filter(s -> s.contains("a")).count();
        long numB = javaRDD.filter(s -> s.contains("b")).count();

        System.out.println("Lines with a: " + numA + ", lines with b: " + numB);
    }

}
