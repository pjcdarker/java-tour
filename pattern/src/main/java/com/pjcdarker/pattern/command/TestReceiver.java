package com.pjcdarker.pattern.command;

/**
 * @author pjc
 * @create 2016-09-10
 */
public class TestReceiver implements Receiver {

    public static final TestReceiver INSTANCE = new TestReceiver();

    @Override
    public void product() {
        System.out.println("product......");
    }

    @Override
    public void test() {
        System.out.println("test....");
    }
}
