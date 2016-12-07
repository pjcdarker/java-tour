package com.pjcdarker.pattern.chain;

/**
 * @author pjc
 * @create 2016-09-10
 */
public class ProductRequestHandler extends RequestHandler {
    public static final ProductRequestHandler INSTANCE = new ProductRequestHandler();

    @Override
    public void handler(HandleType handleType, Chain chain) {
        System.out.println("come in ProductRequestHandler....");
        if (HandleType.PRODUCT.equals(handleType)) {
            System.out.println("product request...");
            return;
        }
        System.out.println("ProductRequestHandler chain to other Request handle....");
        chain.handler(handleType, chain);
    }
}
