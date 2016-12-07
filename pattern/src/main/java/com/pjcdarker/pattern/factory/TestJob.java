package com.pjcdarker.pattern.factory;

/**
 * @author pjc
 * @create 2016-10-02
 */
public class TestJob implements Job {
    @Override
    public void name() {
        System.out.println("test job...");
    }
}
