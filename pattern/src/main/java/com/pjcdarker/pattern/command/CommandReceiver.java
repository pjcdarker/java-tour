package com.pjcdarker.pattern.command;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pjc
 * @create 2016-09-10
 */
public class CommandReceiver {
    public static final CommandReceiver INSTANCE = new CommandReceiver();
    private List<Command> command;

    private CommandReceiver() {
        this.command = new ArrayList<>();
    }

    public void add(Command command) {
        this.command.add(command);
    }

    public void exector() {
        this.command.parallelStream().forEach(command -> {
            command.execute();
        });
        this.command.clear();
    }
}
