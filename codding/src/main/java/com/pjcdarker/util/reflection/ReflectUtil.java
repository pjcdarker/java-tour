package com.pjcdarker.util.reflection;

import java.lang.invoke.*;
import java.lang.reflect.Method;

/**
 * @author pjcdarker
 * @created 9/20/2017.
 */
public class ReflectUtil {


    public static void compile(Method method) throws Throwable {

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle handle = lookup.unreflect(method);
        method.setAccessible(true);

        MethodType invokedType = MethodType.methodType(Type.class);
        MethodType methodType = MethodType.methodType(String.class);

        //MethodHandle handle = lookup.findVirtual(Type.class, "apply", methodType);
        //MethodType actualMethodType = MethodType.methodType(String.class, method.getDeclaringClass());
        //MethodHandle handle = lookup.findSpecial(Consumer.class, "accept", actualMethodType, ReflectUtil.class);

        CallSite callSite = LambdaMetafactory.metafactory(lookup, "apply", invokedType, methodType, handle, methodType);
        callSite.getTarget().invoke();
    }

    public static void invokeMethod() throws Throwable {
        // 1. Retrieves a Lookup
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        // 2. Creates a MethodType(return type   argument class)
        MethodType methodType = MethodType.methodType(boolean.class, String.class);

        // 3. Find the MethodHandle(class, name of method)
        MethodHandle handle = lookup.findVirtual(ReflectUtil.class, "navigateToUrl", methodType);

        ReflectUtil reflectUtil = new ReflectUtil();

        // 4. Invoke the method
        boolean b = (boolean) handle.invokeExact(reflectUtil, "test");

        System.out.println(b);
    }

    public boolean navigateToUrl(String strUrl) {
        return true;
    }

    public static void main(String[] args) throws Throwable {
        ReflectUtil.invokeMethod();
    }

    @FunctionalInterface
    public interface Type {
        String apply();
    }

}


