package com.pjcdarker.pattern.command;

/**
 * @author pjc
 * @create 2016-09-10
 */
public class TestCommand implements Command {

    private Receiver receiver;

    public TestCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        this.receiver.test();
    }
}
