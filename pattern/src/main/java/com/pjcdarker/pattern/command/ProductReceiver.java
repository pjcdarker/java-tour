package com.pjcdarker.pattern.command;

/**
 * @author pjc
 * @create 2016-09-10
 */
public class ProductReceiver implements Receiver {

    public static final ProductReceiver INSTANCE = new ProductReceiver();

    @Override
    public void product() {
        System.out.println("product......");
    }

    @Override
    public void test() {
        System.out.println("test....");
    }
}
