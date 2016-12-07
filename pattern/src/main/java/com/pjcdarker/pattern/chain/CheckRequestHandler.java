package com.pjcdarker.pattern.chain;

/**
 * @author pjc
 * @create 2016-09-27
 */
public class CheckRequestHandler extends RequestHandler {

    public static final CheckRequestHandler INSTANCE = new CheckRequestHandler();

    @Override
    public void handler(HandleType handleType, Chain chain) {
        System.out.println("come in CheckRequestHandler....");
        if (HandleType.CHECK.equals(handleType)) {
            System.out.println("check....");
            return;
        }
        System.out.println("CheckRequestHandler chain to other Request handle....");
        chain.handler(handleType, chain);
    }

}
