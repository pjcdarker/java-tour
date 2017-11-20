package com.pjcdarker.learn;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

/**
 * @author pjcdarker
 * @created 11/20/2017.
 */
public class MethodHandleAccess {

    private String code;

    public MethodHandleAccess(String code) {
        this.code = code;
    }

    public static Method nonArgsReflectAccess() {
        Method method = null;
        try {
            method = MethodHandleAccess.class.getDeclaredMethod("printf");
            method.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;
    }

    public static Method reflectAccess() {
        Method method = null;
        try {
            method = MethodHandleAccess.class.getDeclaredMethod("printf", String.class);
            method.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;
    }

    public static MethodHandle nonArgsMhAccess() {
        MethodHandle mh = null;
        try {
            mh = MethodHandles.lookup().findVirtual(MethodHandleAccess.class, "printf", MethodType.methodType(void.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return mh;
    }

    public static MethodHandle mhAccess() {
        MethodHandle mh = null;
        try {
            mh = MethodHandles.lookup().findVirtual(MethodHandleAccess.class, "printf", MethodType.methodType(void.class, String.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return mh;
    }


    private void printf() {
        System.out.println("This is " + code);
    }

    private void printf(String msg) {
        System.out.println(msg + " : " + code);
    }
}
