package com.pjcdarker.pattern.factory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author pjc
 * @create 2016-10-02
 */
public class FactoryTest {

    private static JobFactory jobFactory;

    @BeforeAll
    public static void factory() {
        jobFactory = JobFactory.factory(builder -> {
            builder.builder(JobType.TEST, TestJob::new);
            builder.builder(JobType.PRODUCT, () -> {
                return () -> System.out.println("product job...");
            });
        });
    }

    @Test
    public void testJob() {
        Job testJob = jobFactory.create(JobType.TEST);
        testJob.name();
    }

    @Test
    public void productJob() {
        Job productJob = jobFactory.create(JobType.PRODUCT);
        productJob.name();
    }
}
