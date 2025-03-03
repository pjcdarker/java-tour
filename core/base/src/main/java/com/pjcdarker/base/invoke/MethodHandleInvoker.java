package com.pjcdarker.base.invoke;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MethodHandleInvoker<T> {

    private final Class<T> mhClass;

    public MethodHandleInvoker(Class<T> mhClass) {
        this.mhClass = mhClass;
    }

    public Object invokeStatic(String method, Class<?> rtype, Object... args) throws Throwable {
        final MethodType methodType = methodType(rtype, args);
        final MethodHandle mh = MethodHandles
            .lookup()
            .findStatic(this.mhClass, method, methodType);

        return mh.invokeWithArguments(extractActualArguments(args));
    }

    public Object invoke(String method, Class<?> rtype, Object... args) throws Throwable {
        MethodHandle mh = MethodHandles.lookup().findVirtual(this.mhClass, method, methodType(rtype, args));
        return mh.invokeWithArguments(arguments(args));
    }

    private MethodType methodType(Class<?> rtype, Object[] args) {
        Class<?>[] paramTypes = Arrays.stream(args)
                                      .map(e -> {
                                          if (e instanceof TypeValue typeValue) {
                                              return typeValue.typeClass;
                                          }
                                          return e.getClass();
                                      })
                                      .toList()
                                      .toArray(new Class<?>[]{});

        return MethodType.methodType(rtype, paramTypes);
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
