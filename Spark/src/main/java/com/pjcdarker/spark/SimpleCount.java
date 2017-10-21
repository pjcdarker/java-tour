package com.pjcdarker.spark;

import org.apache.hadoop.conf.Configuration;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

/**
 * @author pjc
 * @create 10/20/2016
 */
public class SimpleCount {

    public static void main(String[] args) {
        String path = "src/main/resources/spark.md";
        textFileCount(path);
    }

    /**
     * @param path test/  *.txt  test.gz
     */
    public static void textFileCount(String path) {
        // local  * Thread
        SparkConf sparkConf = new SparkConf().setMaster("spark://192.168.1.134:7077").setAppName("textFileCount");

        // tells Spark how to access a cluster
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
        JavaRDD<String> rdd = javaSparkContext.textFile(path).cache();

        long numA = rdd.filter(s -> s.contains("a")).count();
        long numB = rdd.filter(s -> s.contains("b")).count();
        int length = rdd.map(s -> s.length()).reduce((len1, len2) -> len1 + len2);

        System.out.println("Lines with a: " + numA + ", lines with b: " + numB);
        System.out.println("length: " + length);

        rdd.mapToPair(s -> new Tuple2<>(s, 1))
                .reduceByKey((t1, t2) -> t1 + t2)
                .foreach(s -> System.out.println(s._1 + "==================" + s._2));

    }

    public static void wholeTextFilesCount(String path) {
        JavaSparkContext javaSparkContext = SparkContexts.local("wholeTextFilesCount");
        JavaPairRDD<String, String> pairRDD = javaSparkContext.wholeTextFiles(path).cache();

        int partitions = pairRDD.getNumPartitions();
        long count = pairRDD.count();

        Tuple2 tuple2 = pairRDD.reduce((k, v) -> new Tuple2<String, String>(k._1, k._2));

        System.out.println("partitions: " + partitions);
        System.out.println("count: " + count);
        System.out.println("tuple2_1: " + tuple2._1);
        System.out.println("tuple2_2: " + tuple2._2);
        System.out.println("tuple2: " + tuple2.toString());
        javaSparkContext.close();
    }

    public static void removeData(String path) {
        List<Integer> lists = Arrays.asList(1, 2, 3, 4, 5);
        JavaSparkContext sparkContext = SparkContexts.local("removeData");
        JavaRDD<Integer> rdd = sparkContext.parallelize(lists).cache();

        // Memory Deserialized 1x Replicated
        String description = rdd.getStorageLevel().description();
        System.out.println(description);

        rdd.unpersist();

        // Serialized 1x Replicated
        description = rdd.getStorageLevel().description();
        System.out.println(description);
    }


    public static void broadcastVar() {
        JavaSparkContext sparkContext = SparkContexts.local("broadcastVar");
        Broadcast<int[]> broadcastVar = sparkContext.broadcast(new int[]{1, 2, 3});
        int[] values = broadcastVar.value();
        System.out.println(Arrays.toString(values));
    }

    public static void printEle() {
        List<Integer> lists = Arrays.asList(1, 2, 3, 4, 5);
        JavaSparkContext sparkContext = SparkContexts.local("printElement");
        JavaRDD<Integer> rdd = sparkContext.parallelize(lists).cache();
        // java.io.NotSerializableException: java.io.PrintStream
        // rdd.foreach(System.out::println);
        rdd.take(10).forEach(System.out::println);
    }

    public static void parallel() {
        List<Integer> lists = Arrays.asList(1, 2, 3, 4, 5);
        JavaSparkContext sparkContext = SparkContexts.local("parallel");
        JavaRDD<Integer> rdd = sparkContext.parallelize(lists).cache();

        long count = rdd.count();
        System.out.println("count : " + count);

        Integer sum = rdd.reduce((n1, n2) -> {
            System.out.println("currentThread name : " + Thread.currentThread().getName());
            return n1 + n2;
        });
        System.out.println("sum: " + sum);
    }

    public static void sequenceFileCount(String path) {
        JavaSparkContext sparkContext = SparkContexts.local("sequenceFileCount");
        JavaPairRDD<String, Integer> pairRDD = sparkContext.sequenceFile(path, String.class, Integer.class);
        pairRDD.foreach((intWritableTextTuple2 -> {
            String key = intWritableTextTuple2._1.toString();
            String value = intWritableTextTuple2._2.toString();
            System.out.println(key + "==" + value);
        }));
    }

    public static void hadoopRDDCount() {
        JavaSparkContext javaSparkContext = SparkContexts.local("hadoopRDDCount");
        Configuration hadoopConf = new Configuration();
        JavaPairRDD<String, Integer> pairRDD = javaSparkContext.newAPIHadoopRDD(hadoopConf, null, String.class, Integer.class);
        pairRDD.foreach((intWritableTextTuple2 -> {
            String key = intWritableTextTuple2._1.toString();
            Integer value = intWritableTextTuple2._2;
            System.out.println(key + "==" + value);
        }));
    }
}
