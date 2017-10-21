package com.pjcdarker.util;

import org.junit.Test;

import java.util.List;

/**
 * @author pjcdarker
 * @created 9/15/2017.
 */
public class TestScannerRunner {


    @Test
    public void scan() {
        List<Class<?>> results = ScannerRunner.findClassScanPackage("com.pjcdarker.util.http");
        results.forEach(r -> System.out.println(r.getCanonicalName()));
    }
}
