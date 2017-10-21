package com.pjcdarker.beam;

import sun.misc.Unsafe;

/**
 * @author pjcdarker
 * @created 5/20/2017.
 */
public class Simple {

    public static void main(String[] args) {
        Unsafe.getUnsafe().addressSize();
    }
}
