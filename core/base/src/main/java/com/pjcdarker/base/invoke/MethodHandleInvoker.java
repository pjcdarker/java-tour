package com.pjcdarker.base.invoke;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MethodHandleInvoker<T> {

    private final Class<T> mhClass;

    public MethodHandleInvoker(Class<T> mhClass) {
        this.mhClass = mhClass;
    }

    public Object invokeStatic(String method, Class<?> rtype, Object... args) throws Throwable {
        final MethodType methodType = MethodType.methodType(rtype, paramTypes(args));
        final MethodHandle mh = MethodHandles
            .lookup()
            .findStatic(this.mhClass, method, methodType);

        return mh.invokeWithArguments(extractActualArguments(args));
    }

    public Object invoke(String method, Class<?> rtype, Object... args) throws Throwable {
        MethodHandle mh = MethodHandles.lookup().findVirtual(this.mhClass, method,
            MethodType.methodType(rtype, paramTypes(args)));
        return mh.invokeWithArguments(arguments(args));
    }

    public Object invokeUnreflect(String method, Object... args) throws Throwable {
        Method m = this.mhClass.getDeclaredMethod(method, paramTypes(args));
        MethodHandle mh;
        if (Modifier.isPrivate(m.getModifiers())) {
            mh = MethodHandles.privateLookupIn(this.mhClass, MethodHandles.lookup()).unreflect(m);
        } else {
            mh = MethodHandles.lookup().unreflect(m);
        }

        if (Modifier.isStatic(m.getModifiers())) {
            return mh.invokeWithArguments(extractActualArguments(args));
        }

        return mh.invokeWithArguments(arguments(args));
    }


    private Class<?>[] paramTypes(Object[] args) {
        return Arrays.stream(args)
                     .map(e -> {
                         if (e instanceof TypeValue typeValue) {
                             return typeValue.typeClass;
                         }
                         return e.getClass();
                     })
                     .toList()
                     .toArray(new Class<?>[]{});
    }

    private List<Object> arguments(Object[] args) throws Exception {
        Object instance = this.mhClass.getDeclaredConstructor().newInstance();
        List<Object> arguments = new ArrayList<>();
        arguments.add(instance);
        arguments.addAll(extractActualArguments(args));

        return arguments;
    }

    private List<Object> extractActualArguments(Object[] args) {
        return Arrays.stream(args)
                     .map(e -> {
                         if (e instanceof TypeValue typeValue) {
                             return typeValue.value;
                         }
                         return e;
                     })
                     .toList();
    }


    public record TypeValue(Class<?> typeClass, Object value) {

        public static TypeValue of(Class<?> typeClass, Object value) {
            return new TypeValue(typeClass, value);
        }
    }
}
