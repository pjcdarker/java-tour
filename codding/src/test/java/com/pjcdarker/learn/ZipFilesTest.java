package com.pjcdarker.learn;

import com.pjcdarker.learn.io.zip.ZipFiles;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author pjcdarker
 * @created 10/22/2017.
 */
public class ZipFilesTest {

    @Test
    public void unzip() {
        String zipFile = "src/main/resources/test.zip";
        String targetPath = "src/main/resources/out";
        boolean result = ZipFiles.unzip(zipFile, targetPath);

        Assertions.assertTrue(result, "result:true");
    }

    @Test
    public void compress() {
        final String sourceFile = "src/main/resources/out";
        final String outFile = "src/main/resources/test1.zip";
        ZipFiles.compress(sourceFile, outFile);
    }
}
