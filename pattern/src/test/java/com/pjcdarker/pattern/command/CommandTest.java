package com.pjcdarker.pattern.command;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author pjc
 * @create 2016-10-02
 */
public class CommandTest {

    private static CommandReceiver commandReceiver;

    @BeforeClass
    public static void before() {
        commandReceiver = CommandReceiver.INSTANCE;
    }

    @Test
    public void testCommand() {
        commandReceiver.add(new TestCommand(TestReceiver.INSTANCE));
        commandReceiver.exector();
    }

    @Test
    public void productCommand() {
        commandReceiver.add(new ProductCommand(ProductReceiver.INSTANCE));
        commandReceiver.exector();
    }

}
