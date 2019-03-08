package com.pjcdarker.base.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author pjcdarker
 * @created 10/22/2017.
 */
public class FileHelperTest {

    @Test
    public void unzip() {
        String zipFile = "src/main/resources/test.zip";
        String targetPath = "src/main/resources/out";
        boolean result = FileHelper.unzip(zipFile, targetPath);

        Assertions.assertTrue(result, "result:true");
    }

    @Test
    public void compress() {
        final String sourceFile = "src/main/resources/out";
        final String outFile = "src/main/resources/test1.zip";
        FileHelper.compress(sourceFile, outFile);
    }
}
