package com.pjcdarker.util;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;

/**
 * @author pjcdarker
 * @created 5/9/2017.
 */
public class EnumUtils {

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

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 5, 6, 7, 8, 9);

        list.stream().filter(i -> i > 4)
                .forEach(System.out::println);

    }
}
