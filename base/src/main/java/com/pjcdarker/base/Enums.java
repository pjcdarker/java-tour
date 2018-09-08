package com.pjcdarker.base;

import java.lang.reflect.Array;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author pjcdarker
 */
public class Enums {

    public static <T, E extends Enum> Function<T, E> lookupMap(Class clazz, Function<E, T> mapper) {
        E[] emptyArray = (E[]) Array.newInstance(clazz, 0);
        return lookupMap((E[]) EnumSet.allOf(clazz).toArray(emptyArray), mapper);
    }

    public static <T, E extends Enum> Function<T, E> lookupMap(E[] values, Function<E, T> mapper) {
        Map<T, E> index = new HashMap<>(values.length);
        for (E value : values) {
            index.put(mapper.apply(value), value);
        }
        return (T key) -> index.get(key);
    }
}
