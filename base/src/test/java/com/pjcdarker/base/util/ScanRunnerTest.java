package com.pjcdarker.base.util;

import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author pjcdarker
 * @created 10/21/2017.
 */
public class ScanRunnerTest {

    @Test
    public void scan() {
        List<Class<?>> results = ScanRunner.findClassScanPackage("com.pjcdarker.base.net");
        results.forEach(r -> System.out.println(r.getCanonicalName()));
    }
}
