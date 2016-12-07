package com.pjcdarker.pattern.command;

/**
 * @author pjc
 * @create 2016-09-10
 */
public class ProductCommand implements Command {

    private Receiver receiver;

    public ProductCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        this.receiver.product();
    }
}
