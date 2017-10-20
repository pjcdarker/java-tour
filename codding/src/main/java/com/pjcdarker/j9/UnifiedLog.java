package com.pjcdarker.j9;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author pjcdarker
 * @created 9/23/2017.
 */
public class UnifiedLog {

    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static void print() {
        logger.log(Level.INFO, "hello UnifiedLog");
    }

    public static void main(String[] args) {
        print();
    }
}
