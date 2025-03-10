package com.pjcdarker.base.invoke;

import com.pjcdarker.base.invoke.MethodHandleInvoker.TypeValue;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.WrongMethodTypeException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import org.junit.jupiter.api.TestFactory;

public class MethodHandleInvokerTest {


    @BeforeEach
    void beforeEachSetup() {
        MH.result = "___empty___";
    }

    @TestFactory
    List<DynamicTest> should_invoke_instance() {
        MethodHandleInvoker<MH> methodHandleInvoker = new MethodHandleInvoker<>(MH.class);
        final List<String> values = Arrays.asList(null, "a", "a,b", "a,b,c");
        String method = "invoke_instance_method";

        List<DynamicTest> dynamicTests = new ArrayList<>();
        values.forEach(value -> {
            Object[] args = value == null ? new Object[0] : value.split(",");
            dynamicTests.add(dynamicTest(method + "_args_" + args.length,
                () -> {
                    methodHandleInvoker.invoke(method, void.class, args);
                    assertEquals(method + "_args_" + args.length, MH.result);
                }));
        });

        // args contain null value
        dynamicTests.add(dynamicTest("invoke_instance_method_args_which_contain_null_value",
            () -> {
                methodHandleInvoker.invoke(
                    method, void.class,
                    "dog", TypeValue.of(String.class, null));
                assertEquals(method + "_args_2", MH.result);
            }));

        dynamicTests.add(dynamicTest("invoke_instance_method_with_return_value",
            () -> {
                Object result = methodHandleInvoker.invoke(
                    "invoke_instance_method_with_return_value",
                    String.class, "dog");
                assertEquals("hi:dog", result);
            }));

        return dynamicTests;
    }

    @TestFactory
    List<DynamicTest> should_invoke_static() {
        MethodHandleInvoker<MH> methodHandleInvoker = new MethodHandleInvoker<>(MH.class);
        final List<String> values = Arrays.asList(null, "a", "a,b", "a,b,c");
        String method = "invoke_static_method";

        List<DynamicTest> dynamicTests = new ArrayList<>();
        values.forEach(value -> {
            Object[] args = value == null ? new Object[0] : value.split(",");
            dynamicTests.add(dynamicTest(method + "_args_" + args.length,
                () -> {
                    methodHandleInvoker.invokeStatic(method, void.class, args);
                    assertEquals(method + "_args_" + args.length, MH.result);
                }));
        });

        // args contain null value
        dynamicTests.add(dynamicTest("invoke_static_method_args_which_contain_null_value",
            () -> {
                methodHandleInvoker.invokeStatic(
                    method, void.class,
                    "dog", TypeValue.of(String.class, null));
                assertEquals(method + "_args_2", MH.result);
            }));

        return dynamicTests;
    }

    @TestFactory
    List<DynamicTest> should_invoke_unreflect() {
        MethodHandleInvoker<MH> methodHandleInvoker = new MethodHandleInvoker<>(MH.class);

        Map<String, List<String>> staticTests = new HashMap<>();
        staticTests.put("invoke_static_method", Arrays.asList(null, "a", "a,b", "a,b,c"));
        staticTests.put("invoke_private_static_method", Arrays.asList(null, "a", "a,b", "a,b,c"));
        staticTests.put("invoke_instance_method", Arrays.asList(null, "a", "a,b", "a,b,c"));
        staticTests.put("invoke_private_instance_method", Arrays.asList(null, "a", "a,b", "a,b,c"));

        List<DynamicTest> dynamicTests = new ArrayList<>();
        staticTests.forEach((method, values) -> {
            values.forEach(value -> {
                Object[] args = value == null ? new Object[0] : value.split(",");
                dynamicTests.add(dynamicTest(method + "_args_" + args.length,
                    () -> {
                        methodHandleInvoker.invokeUnreflect(method, args);
                        assertEquals(method + "_args_" + args.length, MH.result);
                    }));
            });
        });

        // args contain null value
        dynamicTests.add(dynamicTest("invoke_instance_method_args_which_contain_null_value",
            () -> {
                methodHandleInvoker.invokeUnreflect(
                    "invoke_instance_method",
                    "dog", TypeValue.of(String.class, null));
                assertEquals("invoke_instance_method_args_2", MH.result);
            }));

        return dynamicTests;
    }

    @TestFactory
    List<DynamicTest> should_invoke() throws Throwable {
        MethodHandle mh = MethodHandles
            .lookup()
            .findVirtual(Component.class, "wrap", MethodType.methodType(Component.class, Component.class));

        List<DynamicTest> dynamicTests = new ArrayList<>();
        final Component component = new Component();
        final RedisComponent redisComponent = new RedisComponent();

        dynamicTests.add(dynamicTest("Type-Type", () -> {
            Component result = (Component) mh.invoke(component, component);
            assertEquals("Component", result.name());
        }));

        dynamicTests.add(dynamicTest("Type-SubType", () -> {
            Component result = (Component) mh.invoke(component, redisComponent);
            assertEquals("Component", result.name());
        }));

        dynamicTests.add(dynamicTest("SubType-Type", () -> {
            Component result = (Component) mh.invoke(redisComponent, component);
            assertEquals("RedisComponent", result.name());
        }));

        dynamicTests.add(dynamicTest("SubType-SubType", () -> {
            Component result = (Component) mh.invoke(redisComponent, redisComponent);
            assertEquals("RedisComponent", result.name());
        }));

        return dynamicTests;
    }

    @TestFactory
    List<DynamicTest> should_invoke_exact() throws Throwable {
        MethodHandle mh = MethodHandles
            .lookup()
            .findVirtual(Component.class, "wrap", MethodType.methodType(Component.class, Component.class));

        List<DynamicTest> dynamicTests = new ArrayList<>();
        final Component component = new Component();
        final RedisComponent redisComponent = new RedisComponent();

        dynamicTests.add(dynamicTest("Type-Type", () -> {
            Component result = (Component) mh.invokeExact(component, component);
            assertEquals("Component", result.name());
        }));

        dynamicTests.add(dynamicTest("Type-SubType will throw an WrongMethodTypeException", () -> {
            assertThrows(WrongMethodTypeException.class, () -> mh.invokeExact(component, redisComponent));
        }));

        dynamicTests.add(dynamicTest("SubType-Type will throw an WrongMethodTypeException", () -> {
            assertThrows(WrongMethodTypeException.class, () -> mh.invokeExact(redisComponent, component));
        }));

        dynamicTests.add(dynamicTest("SubType-SubType will throw an WrongMethodTypeException", () -> {
            assertThrows(WrongMethodTypeException.class, () -> mh.invokeExact(redisComponent, redisComponent));
        }));

        return dynamicTests;
    }

    public static class MH {

        private static String result = "";

        private static void invoke_private_static_method() {
            result = "invoke_private_static_method_args_0";
        }

        private static void invoke_private_static_method(String arg) {
            result = "invoke_private_static_method_args_1";
        }

        private static void invoke_private_static_method(String arg1, String arg2) {
            result = "invoke_private_static_method_args_2";
        }

        private static void invoke_private_static_method(String arg1, String arg2, String arg3) {
            result = "invoke_private_static_method_args_3";
        }

        public static void invoke_static_method() {
            result = "invoke_static_method_args_0";
        }

        public static void invoke_static_method(String arg) {
            result = "invoke_static_method_args_1";
        }

        public static void invoke_static_method(String arg1, String arg2) {
            result = "invoke_static_method_args_2";
        }

        public static void invoke_static_method(String arg1, String arg2, String arg3) {
            result = "invoke_static_method_args_3";
        }

        public void invoke_instance_method() {
            result = "invoke_instance_method_args_0";

        }

        public void invoke_instance_method(String arg) {
            result = "invoke_instance_method_args_1";
        }

        public void invoke_instance_method(String arg1, String arg2) {
            result = "invoke_instance_method_args_2";
        }

        public void invoke_instance_method(String arg1, String arg2, String arg3) {
            result = "invoke_instance_method_args_3";
        }

        private void invoke_private_instance_method() {
            result = "invoke_private_instance_method_args_0";

        }

        private void invoke_private_instance_method(String arg) {
            result = "invoke_private_instance_method_args_1";

        }

        private void invoke_private_instance_method(String arg1, String arg2) {
            result = "invoke_private_instance_method_args_2";
        }

        private void invoke_private_instance_method(String arg1, String arg2, String arg3) {
            result = "invoke_private_instance_method_args_3";
        }

        public String invoke_instance_method_with_return_value(String msg) {
            return "hi:" + msg;
        }

    }

    public static class Component {

        public String name() {
            return "Component";
        }

        public Component wrap(Component component) {
            return new Component();
        }
    }

    public static class RedisComponent extends Component {

        private final String name;

        public RedisComponent() {
            this.name = "RedisComponent";
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public Component wrap(Component component) {
            return new RedisComponent();
        }
    }

}
