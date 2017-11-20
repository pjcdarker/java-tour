package com.pjcdarker.j9;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.Arrays;

/**
 * @author pjcdarker
 * @created 9/23/2017.
 */
public class VarHandleDemo {

    public void arrayElementVarHandle() {
        String[] sa = new String[]{"abc"};
        VarHandle avh = MethodHandles.arrayElementVarHandle(String[].class);
        boolean r = avh.compareAndSet(sa, 0, "abc", "d");
        System.out.println(r);
        System.out.println(Arrays.toString(sa));
    }

    public MethodHandle toMethodHandle() throws NoSuchFieldException, IllegalAccessException {
        VarHandle vh = MethodHandles.lookup()
                                    .in(Eoo.class)
                                    .findVarHandle(Eoo.class, "s", String.class);

        VarHandle.AccessMode am = VarHandle.AccessMode.valueFromMethodName(VarHandle.AccessMode.GET_AND_SET.methodName());
        MethodHandle mh = vh.toMethodHandle(am);
        return mh;
    }

    public static void main(String[] args) throws Throwable {
        VarHandleDemo vh = new VarHandleDemo();
        MethodHandle mh = vh.toMethodHandle();
        Eoo e = new Eoo();
        Object ss = mh.invoke(e, "get");
        System.out.println(ss);
    }

}

class Eoo {
    int i;
    String s;
}
