package com.pjcdarker.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;

import java.util.Arrays;
import java.util.List;

/**
 * @author pjc
 * @Created 1/6/2017.
 */
public class Operations {

    public static void avg() {
        JavaRDD<Integer> rdd = getRDD();
        Function2<Integer, Integer, Integer> seqOp = (v1, v2) -> {
            System.out.println(v1 + "==seqOp===" + v2);
            return v1 + v2;
        };

        Function2<Integer, Integer, Integer> combOp = (v1, v2) -> {
            System.out.println(v1 + "==combOp===" + v2);
            return v1 + v2;
        };

        Integer avgValue = rdd.aggregate(0, seqOp, combOp);
        System.out.println(avgValue);
    }

    public static void fromDB() {
        String url = "jdbc:mysql://192.168.10.134:3306/spark?user=root;password=devroot";
        JavaSparkContext sparkContext = SparkContexts.local("db");

    }


    private static JavaRDD<Integer> getRDD() {
        List<Integer> lists = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        SparkConf sparkConf = new SparkConf().setMaster("local[*]").setAppName("operationsApp");
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
        JavaRDD<Integer> rdd = sparkContext.parallelize(lists).cache();
        return rdd;
    }
}
