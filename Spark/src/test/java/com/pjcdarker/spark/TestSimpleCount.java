package com.pjcdarker.spark;

import org.junit.Test;

/**
 * @author pjc
 * @created 11/6/2016.
 */
public class TestSimpleCount {

    private static final String FILE_PATH = "src/main/resources/spark.md";

    @Test
    public void textCount() {
        SimpleCount.textFileCount(FILE_PATH);
    }

    @Test
    public void wholeTextFilesCount() {
        SimpleCount.wholeTextFilesCount(FILE_PATH);
    }

    @Test
    public void sequenceFileCount() {
        SimpleCount.sequenceFileCount(FILE_PATH);
    }

    @Test
    public void printEle() {
        SimpleCount.printEle();
    }

    @Test
    public void removeData() {
        SimpleCount.removeData(FILE_PATH);
    }

    @Test
    public void broadcastVar() {
        SimpleCount.broadcastVar();
    }

    @Test
    public void parallel() {
        SimpleCount.parallel();
    }
}
