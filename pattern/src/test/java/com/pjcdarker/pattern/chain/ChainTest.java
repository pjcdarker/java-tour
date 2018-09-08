package com.pjcdarker.pattern.chain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author pjc
 * @create 2016-09-26
 */
public class ChainTest {

    private static Chain chain;

    @BeforeAll
    public static void createChain() {
        chain = Chain.INSTANCE;
    }

    @Test
    public void checkHandler() {
        chain.addRequestHandler(CheckRequestHandler.INSTANCE);
        chain.handler(HandleType.CHECK, chain);
    }

    @Test
    public void testHandler() {
        chain.addRequestHandler(TestRequestHandler.INSTANCE);
        chain.handler(HandleType.TEST, chain);
    }

    @Test
    public void productHandler() {
        chain.addRequestHandler(ProductRequestHandler.INSTANCE);
        chain.handler(HandleType.PRODUCT, chain);
    }

    @Test
    public void testRequestHandler() {
        chain.addRequestHandler(ProductRequestHandler.INSTANCE)
             .next(TestRequestHandler.INSTANCE)
             .next(CheckRequestHandler.INSTANCE);

        chain.handler(HandleType.CHECK, chain);
    }
}
