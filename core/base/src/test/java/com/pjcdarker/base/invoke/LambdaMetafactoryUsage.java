package com.pjcdarker.base.invoke;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class LambdaMetafactoryUsage {


    @Test
    void test_non_generic_function() throws Throwable {
        final Method method = Database.class.getDeclaredMethod("invoke_static_method", int.class);
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle implMethodHandle = lookup.unreflect(method);

        final Method lambdaMethod = lambdaMethod(NonGenericFunction.class);

        MethodType invokedType = MethodType.methodType(NonGenericFunction.class);
        MethodType interfaceMethodType = MethodType.methodType(lambdaMethod.getReturnType(),
            lambdaMethod.getParameterTypes());
        MethodType instantiatedMethodType = MethodType.methodType(method.getReturnType(), method.getParameterTypes());

        CallSite callSite = LambdaMetafactory.metafactory(
            lookup,
            "apply",
            invokedType,
            interfaceMethodType,
            implMethodHandle,
            instantiatedMethodType
        );

        final NonGenericFunction target = (NonGenericFunction) callSite.getTarget().invoke();
        final String result = target.apply(1);

        assertEquals("invoke_static_method_1", result);
    }

    @Test
    void test_generic_function() throws Throwable {
        Method method = Database.class.getDeclaredMethod("invoke_instance_method", int.class);
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle methodHandle = lookup.unreflect(method);

        final Method lambdaMethod = lambdaMethod(Function.class);

        MethodType invokedType = MethodType.methodType(Function.class, method.getDeclaringClass());
        MethodType interfaceMethodType = MethodType.methodType(lambdaMethod.getReturnType(),
            lambdaMethod.getParameterTypes());
        // int(primitive) type need to cast Integer(Object) type
        MethodType instantiatedMethodType = MethodType.methodType(method.getReturnType(),
            toObjectType(method.getParameterTypes()));

        CallSite callSite = LambdaMetafactory.metafactory(
            // method handle lookup
            lookup,
            "apply",
            // type to be implemented and captured objects
            invokedType,
            // type erasure, generic types will be Object
            interfaceMethodType,
            methodHandle,
            // real signature and return type
            instantiatedMethodType
        );

        Function function = (Function) callSite.getTarget().bindTo(new Database()).invokeExact();

        Object result = function.apply(2);

        assertEquals("invoke_instance_method_2", result);
    }

    private Class<?>[] toObjectType(Class<?>[] types) {
        for (int i = 0; i < types.length; i++) {
            types[i] = toObjectType(types[i]);
        }
        return types;
    }

    private Class<?> toObjectType(Class<?> type) {
        if (!type.isPrimitive()) {
            return type;
        }

        // Boolean.TYPE,
        //     Character.TYPE,
        //     Byte.TYPE,
        //     Short.TYPE,
        //     Integer.TYPE,
        //     Long.TYPE,
        //     Float.TYPE,
        //     Double.TYPE,
        //     Void.TYPE
        if ("int".equals(type.getSimpleName())) {
            return Integer.class;
        }

        return type;
    }

    private static Method lambdaMethod(Class<?> lambdaClass) {
        final Method[] methods = lambdaClass.getDeclaredMethods();
        final List<Method> interfaceMethods = Arrays
            .stream(methods)
            .filter(e -> Modifier.isAbstract(e.getModifiers()))
            .toList();

        if (interfaceMethods.isEmpty()) {
            throw new IllegalStateException("No interface methods found");
        }

        if (interfaceMethods.size() > 1) {
            throw new IllegalStateException(lambdaClass.getName() + " isn't functional interface");
        }

        return interfaceMethods.getFirst();
    }


    @FunctionalInterface
    public interface NonGenericFunction {

        String apply(int id);
    }

    public static class Database {

        public static String invoke_static_method(int id) {
            return "invoke_static_method_" + id;
        }

        public String invoke_instance_method(int input) {
            return "invoke_instance_method_" + input;
        }
    }
}
