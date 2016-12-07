package com.pjcdarker.pattern.factory;

import java.util.function.Supplier;

/**
 * @author pjc
 * @create 2016-10-02
 */
@FunctionalInterface
public interface Builder {
   void builder(JobType jobType, Supplier<Job> supplier);
}
