package com.pjcdarker.util.reflection;

import java.lang.invoke.*;
import java.lang.reflect.Method;

/**
 * @author pjcdarker
 * @created 9/20/2017.
 */
public class Execute {

    public interface ProcessBase {
    }

    @FunctionalInterface
    public interface Step {
        Boolean apply();
    }

    public Step getMethodFromStepid(ProcessBase process, int stepid) {
        try {
            final MethodHandles.Lookup caller = MethodHandles.lookup();

            final String mname = "step_" + stepid;
            // new java8 method reference stuff
            final Method method = process.getClass().getMethod(mname);
            method.setAccessible(true);

            // standard reflection stuff
            final MethodHandle unreflect = caller.unreflect(method);

            final MethodType type = MethodType.methodType(Boolean.class);
            final MethodType stepType = MethodType.methodType(Step.class);

            final CallSite site = LambdaMetafactory.metafactory(
                    caller, "apply", stepType, type, unreflect, type); // damn
            // convert site to my method reference
            final MethodHandle factory = site.getTarget();
            final Step step = (Step) factory.invoke();
            return step;
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
