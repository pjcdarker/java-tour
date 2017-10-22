package com.pjcdarker.learn.poi;

import java.lang.annotation.*;

/**
 * @author pjcdarker
 * @created 10/21/2017.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MapperCell {

    String name();

    int order();
}
