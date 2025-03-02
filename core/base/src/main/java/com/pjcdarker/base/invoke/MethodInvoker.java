package com.pjcdarker.base.invoke;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

/**
 * @author pjcdarker
 */
public class MethodInvoker {

    public interface ProcessBase {

    }

    @FunctionalInterface
    public interface Step {

        Boolean apply();
    }

    @FunctionalInterface
    public interface Type {

        String apply();
    }

    public MethodInvoker.Step getMethodFromStepid(MethodInvoker.ProcessBase process, int stepid) {
        try {
            final MethodHandles.Lookup caller = MethodHandles.lookup();

            final String mname = "step_" + stepid;
            // new java8 method reference stuff
            final Method method = process.getClass().getMethod(mname);
            method.setAccessible(true);

            // standard reflect stuff
            final MethodHandle unreflect = caller.unreflect(method);

            final MethodType type = MethodType.methodType(Boolean.class);
            final MethodType stepType = MethodType.methodType(MethodInvoker.Step.class);

            final CallSite site = LambdaMetafactory.metafactory(
                caller, "apply", stepType, type, unreflect, type); // damn
            // convert site to my method reference
            final MethodHandle factory = site.getTarget();
            return (MethodInvoker.Step) factory.invoke();

        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

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

    public static void invokeExactMethod(String methodName) throws Throwable {
        // 1. Retrieves a Lookup
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        // 2. Creates a MethodType(return type   argument class)
        MethodType methodType = MethodType.methodType(boolean.class, String.class);

        // 3. Find the MethodHandle(class, name of method)
        MethodHandle handle = lookup.findVirtual(MethodInvoker.class, methodName, methodType);

        MethodInvoker reflectUtil = new MethodInvoker();

        // 4. Invoke the method
        boolean b = (boolean) handle.invokeExact(reflectUtil, "https://github.com/");

        System.out.println(b);
    }

    public static void invokeMethod(Object instance, Method method, Object args) throws Throwable {
        // 1. Retrieves a Lookup
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        // 2. Creates a MethodType(return type   argument class)
        MethodType methodType = MethodType.methodType(method.getReturnType(), method.getParameterTypes());

        // 3. Find the MethodHandle(class, name of method)
        MethodHandle handle = lookup.findVirtual(instance.getClass(), method.getName(), methodType);

        // 4. Invoke the method
        System.out.println(handle.invoke(instance, args));
    }

    public boolean navigateToUrl(String url) {
        System.out.println(url);
        return true;
    }

    public static void main(String[] args) throws Throwable {
        // MethodInvoker.invokeExactMethod("navigateToUrl");

        MethodInvoker instance = new MethodInvoker();

        Method method = instance.getClass().getDeclaredMethod("navigateToUrl", String.class);

        MethodInvoker.invokeMethod(instance, method, "https://github.com/");
    }

}
