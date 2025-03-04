package com.pjcdarker.base.io;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


class ClassScannerTest {

    @Test
    void should_scan_all_classes_by_package() throws Throwable {
        final ClassScanner scanner = new ClassScanner();
        List<Class<?>> results = scanner.scanPackage("com.pjcdarker.base.io");

        assertTrue(results.contains(ClassScannerTest.class));
    }
}
