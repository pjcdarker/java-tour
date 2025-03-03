package com.pjcdarker.base.invoke;

import com.pjcdarker.base.invoke.MethodHandleInvoker.TypeValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class MethodHandleInvokerTest {


    @BeforeEach
    void beforeEachSetup() {
        MH.result = "___empty___";
    }

    @Test
    void should_invoke_without_args() throws Throwable {
        MethodHandleInvoker<MH> methodHandleInvoker = new MethodHandleInvoker<>(MH.class);
        methodHandleInvoker.invoke("invoke_instance_method", void.class);
        assertEquals("invoke_instance_method", MH.result);
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
        '1 arg', 'a'
        '2 args', 'a,b'
        '3 args', 'a,b,c'
        """)
    void should_invoke_instance_method(String displayName, String value) throws Throwable {
        MethodHandleInvoker<MH> methodHandleInvoker = new MethodHandleInvoker<>(MH.class);
        Object[] args = value.split(",");
        methodHandleInvoker.invoke("invoke_instance_method", void.class, args);
        assertEquals("invoke_instance_method_arg_" + args.length, MH.result);
    }

    @Test
    void should_invoke_instance_method_if_args_contain_null_value() throws Throwable {
        MethodHandleInvoker<MH> methodHandleInvoker = new MethodHandleInvoker<>(MH.class);
        String arg = null;
        methodHandleInvoker.invoke("invoke_instance_method", void.class, TypeValue.of(String.class, arg));
        assertEquals("invoke_instance_method_arg_1", MH.result);

        methodHandleInvoker.invoke("invoke_instance_method", void.class, "dog", TypeValue.of(String.class, arg));
        assertEquals("invoke_instance_method_arg_2", MH.result);
    }

    @Test
    void should_invoke_instance_method_with_return_value() throws Throwable {
        MethodHandleInvoker<MH> methodHandleInvoker = new MethodHandleInvoker<>(MH.class);
        Object result = methodHandleInvoker.invoke("invoke_instance_method_with_return_value", String.class, "dog");
        assertEquals("hi:dog", result);
    }

    @Test
    void should_invoke_static_method_without_args() throws Throwable {
        MethodHandleInvoker<MH> methodHandleInvoker = new MethodHandleInvoker<>(MH.class);
        methodHandleInvoker.invokeStatic("invoke_static_method", void.class);
        assertEquals("invoke_static_method", MH.result);
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
        '1 arg', 'a'
        '2 args', 'a,b'
        '3 args', 'a,b,c'
        """)
    void should_invoke_static_method(String displayName, String value) throws Throwable {
        MethodHandleInvoker<MH> methodHandleInvoker = new MethodHandleInvoker<>(MH.class);
        Object[] args = value.split(",");
        methodHandleInvoker.invokeStatic("invoke_static_method", void.class, args);
        assertEquals("invoke_static_method_arg_" + args.length, MH.result);

        methodHandleInvoker.invokeStatic("invoke_static_method", void.class, "d", "f");
        assertEquals("invoke_static_method_arg_2", MH.result);
    }

    @Test
    void should_invoke_static_method_if_args_contain_null_value() throws Throwable {
        MethodHandleInvoker<MH> methodHandleInvoker = new MethodHandleInvoker<>(MH.class);
        methodHandleInvoker.invokeStatic("invoke_static_method", void.class, TypeValue.of(String.class, null));
        assertEquals("invoke_static_method_arg_1", MH.result);

        methodHandleInvoker.invokeStatic("invoke_static_method", void.class, "dog", TypeValue.of(String.class, null));
        assertEquals("invoke_static_method_arg_2", MH.result);
    }

    public static class MH {

        private static String result = "";

        public static void invoke_static_method() {
            result = "invoke_static_method";
        }

        public static void invoke_static_method(String arg) {
            result = "invoke_static_method_arg_1";
        }

        public static void invoke_static_method(String arg1, String arg2) {
            result = "invoke_static_method_arg_2";
        }

        public static void invoke_static_method(String arg1, String arg2, String arg3) {
            result = "invoke_static_method_arg_3";
        }

        public void invoke_instance_method() {
            result = "invoke_instance_method";

        }

        public void invoke_instance_method(String arg) {
            result = "invoke_instance_method_arg_1";

        }

        public void invoke_instance_method(String arg1, String arg2) {
            result = "invoke_instance_method_arg_2";
        }

        public void invoke_instance_method(String arg1, String arg2, String arg3) {
            result = "invoke_instance_method_arg_3";
        }

        public String invoke_instance_method_with_return_value(String msg) {
            return "hi:" + msg;
        }

    }
}
