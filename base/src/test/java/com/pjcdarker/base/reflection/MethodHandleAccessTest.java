package com.pjcdarker.base.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

/**
 * @author pjcdarker
 * @created 11/20/2017.
 */
public class MethodHandleAccessTest {

    @Test
    public void nonArgsRefAccess() throws InvocationTargetException, IllegalAccessException {
        MethodHandleAccess mha = new MethodHandleAccess("nonArgsRefAccess");
        Object result = MethodHandleAccess.nonArgsReflectAccess().invoke(mha);

        // method returnType is void: java.lang.NullPointerException
        // System.out.println(result.toString());
    }

    @Test
    public void refAccess() throws InvocationTargetException, IllegalAccessException {
        MethodHandleAccess mha = new MethodHandleAccess("refAccess");
        Object result = MethodHandleAccess.reflectAccess().invoke(mha, "This is refAccess ");
    }

    @Test
    public void nonArgsMhAccess() throws Throwable {
        MethodHandleAccess mha = new MethodHandleAccess("nonArgsMhAccess");
        MethodHandleAccess.nonArgsMhAccess().invoke(mha);
    }

    @Test
    public void mhAccess() throws Throwable {
        MethodHandleAccess mha = new MethodHandleAccess("mhAccess");
        Object result = MethodHandleAccess.mhAccess().invoke(mha, "This is mhAccess ");
    }
}
