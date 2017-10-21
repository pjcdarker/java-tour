package com.pjcdarker.j9;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.Arrays;

/**
 * @author pjcdarker
 * @created 9/23/2017.
 */
public class VarHandleDemo {


    public static void arrayElementVarHandle() {
        String[] sa = new String[]{"abc"};
        VarHandle avh = MethodHandles.arrayElementVarHandle(String[].class);
        boolean r = avh.compareAndSet(sa, 0, "abc", "d");
        System.out.println(r);
        System.out.println(Arrays.toString(sa));
    }
}
