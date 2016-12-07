package com.pjcdarker.pattern.chain;

/**
 * @author pjc
 * @create 2016-09-10
 */
public class TestRequestHandler extends RequestHandler {

    public static final TestRequestHandler INSTANCE = new TestRequestHandler();

    @Override
    public void handler(HandleType handleType, Chain chain) {
        System.out.println("come in TestRequestHandler....");
        if (HandleType.TEST.equals(handleType)) {
            System.out.println("test request....");
            return;
        }
        System.out.println("TestRequestHandler chain to other Request handle....");
        chain.handler(handleType, chain);
    }
}
