
package com.pjcdarker.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * {@snippet lang = "kotlin":
 *
 * // add this code in build.gradle.kts
 * tasks.named<Test>("test") {
 *     jvmArgs(
 *         "--add-opens", "java.base/sun.reflect.annotation=ALL-UNNAMED",
 *     )
 * }
 *
 *}
 */
class AnnotationInvocationHandlerTest {

    @Test
    void should_change_annotation_value() throws Throwable {
        MqConsumer annotation = TestMqConsumer.class.getAnnotation(MqConsumer.class);
        assertEquals("main", annotation.group());

        InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
        VarHandle varHandle = MethodHandles
            .privateLookupIn(invocationHandler.getClass(), MethodHandles.lookup())
            .findVarHandle(invocationHandler.getClass(), "memberValues", Map.class);

        Map<String, Object> memberValues = (Map<String, Object>) varHandle.get(invocationHandler);
        memberValues.put("group", "coding");

        assertEquals("coding", annotation.group());
    }

    @MqConsumer(topic = "order")
    public static class TestMqConsumer {

    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    public @interface MqConsumer {

        String group() default "main";

        String topic();

    }
}
