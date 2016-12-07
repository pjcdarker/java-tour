package com.pjcdarker.pattern.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author pjc
 * @create 2016-10-02
 */
public interface JobFactory {

    static JobFactory factory(Consumer<Builder> consumer) {
        Map<JobType, Supplier<Job>> map = new HashMap<>();
        consumer.accept(map::put);
        return name -> map.get(name).get();
    }

    Job create(JobType jobType);
}
