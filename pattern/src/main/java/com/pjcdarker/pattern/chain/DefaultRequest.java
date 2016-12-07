package com.pjcdarker.pattern.chain;

/**
 * @author pjc
 * @create 2016-09-10
 */
public class DefaultRequest extends RequestHandler {

    public static final DefaultRequest INSTANCE = new DefaultRequest();

    @Override
    public void handler(HandleType handleType, Chain chain) {
        System.out.println("come in DefaultRequest....");
        if (HandleType.DEFAULT.equals(handleType)) {
            System.out.println("default request....");
            return;
        }
        System.out.println("DefaultRequest chain to other Request handle....");
        chain.handler(handleType, chain);
    }
}
