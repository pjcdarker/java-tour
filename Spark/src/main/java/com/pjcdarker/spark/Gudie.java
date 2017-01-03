package com.pjcdarker.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

/**
 * @author pjc
 * @create 10/20/2016
 */
public class Gudie {

    public static void textFileCount(String path) {
        // local  * Thread
        SparkConf sparkConf = new SparkConf().setMaster("local[*]").setAppName("GudieApp");

        // tells Spark how to access a cluster
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
        JavaRDD<String> javaRDD = javaSparkContext.textFile(path).cache();

        long numA = javaRDD.filter(s -> s.contains("a")).count();
        long numB = javaRDD.filter(s -> s.contains("b")).count();

        System.out.println("Lines with a: " + numA + ", lines with b: " + numB);
    }

    public static void parallelize() {
        List<Integer> lists = Arrays.asList(1, 2, 3, 4, 5);
        SparkConf sparkConf = new SparkConf().setMaster("local[*]").setAppName("GudieApp");
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
        JavaRDD<Integer> rdd = sparkContext.parallelize(lists).cache();

        // a second parameter  to set the number of partitions
        // JavaRDD<Integer> rdd = sparkContext.parallelize(lists, 4).cache();
        long count = rdd.count();
        System.out.println("count : " + count);

        Integer sum = rdd.reduce((n1, n2) -> {
            System.out.println("currentThread name : " + Thread.currentThread().getName());
            return n1 + n2;
        });
        System.out.println("sum: " + sum);
    }

    public static void removeDate(String path) {
        SparkConf sparkConf = new SparkConf().setMaster("local[*]").setAppName("GudieApp");

        // tells Spark how to access a cluster
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
        JavaRDD<String> javaRDD = javaSparkContext.textFile(path).cache();
    }
}
