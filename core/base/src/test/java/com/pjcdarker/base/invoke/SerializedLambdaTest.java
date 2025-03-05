package com.pjcdarker.base.invoke;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.function.Function;
import java.util.function.Supplier;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class SerializedLambdaTest {


    @Test
    void should_get_method_name_with_lambda_parameter() {
        User user = new User();
        String methodName = getImplMethodName(() -> (SerializedSupplier<String>) user::getName);
        assertEquals("getName", methodName);
    }

    @Test
    void should_get_lambda_reference_return_type() {
        User user = new User();
        SerializedLambda serializedLambda = serializedLambda(() -> (SerializedSupplier<String>) user::getName);
        String returnType = getReturnType(serializedLambda);

        assertEquals("java/lang/String", returnType);
    }

    @Test
    void should_get_lambda_reference_argument_type() {
        User user = new User();
        SerializedLambda serializedLambda = serializedLambda(() -> (SerializedFunction<Integer, User>) user::copyFrom);

        String parameterType = getParameterType(serializedLambda);

        assertEquals("java/lang/Integer", parameterType);
    }

    private String getParameterType(SerializedLambda serializedLambda) {
        return getType(serializedLambda, '(');
    }

    private String getReturnType(SerializedLambda serializedLambda) {
        return getType(serializedLambda, ')');
    }

    private static String getType(SerializedLambda lambda, int startChar) {
        // ()Ljava/lang/String;
        // (Ljava/lang/String;)Ljava/lang/String;
        String type = lambda.getInstantiatedMethodType();
        String objectReferenceTypeSign = "L";
        // exclude start index(+1)
        int startIdx = type.indexOf(startChar) + objectReferenceTypeSign.length() + 1;
        return type.substring(startIdx, type.indexOf(";"));
    }

    private <T extends Serializable> String getImplMethodName(Supplier<T> supplier) {
        return serializedLambda(supplier).getImplMethodName();
    }


    private <T extends Serializable> SerializedLambda serializedLambda(Supplier<T> supplier) {
        T serializedFuncInterfaceInstance = supplier.get();
        try {
            return (SerializedLambda) serializedFuncInterfaceInstance
                .getClass()
                .getDeclaredMethod("writeReplace")
                .invoke(serializedFuncInterfaceInstance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    interface SerializedSupplier<R> extends Supplier<R>, Serializable {

    }

    interface SerializedFunction<T, R> extends Function<T, R>, Serializable {

    }

    public static class User {

        public String name = "admin";

        public String getName() {
            return name;
        }

        public User copyFrom(Integer id) {
            return new User();
        }
    }
}
