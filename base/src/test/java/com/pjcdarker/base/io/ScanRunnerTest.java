package com.pjcdarker.base.io;

import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author pjcdarker
 * @created 10/21/2017.
 */
public class ScanRunnerTest {

    @Test
    public void scan() {
        List<Class<?>> results = ScanHelper.findClassScanPackage("com.pjcdarker.base.reflect");
        results.forEach(r -> System.out.println(r.getCanonicalName()));
    }
}
