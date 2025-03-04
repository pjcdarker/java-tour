package com.pjcdarker.base.io;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class FileCompressionTest {

    String zipFile = "src/main/resources/test.zip";
    String path = "src/main/resources/out";
    String outFile = "src/main/resources/out.zip";

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(outFile));
        Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
                Files.deleteIfExists(path);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Test
    void should_unzip() throws IOException {
        FileCompression fileCompression = new FileCompression();
        fileCompression.unzip(zipFile, path);

        final FileStore zipFileStore = Files.getFileStore(Paths.get(zipFile));
        final FileStore fileStore = Files.getFileStore(Paths.get(path));

        assertEquals(zipFileStore, fileStore);
        assertTrue(fileStore.getTotalSpace() > 0);
    }

    @Test
    void should_zip() throws IOException {
        final FileCompression fileCompression = new FileCompression();
        fileCompression.zip(path, outFile);

        final FileStore fileStore = Files.getFileStore(Paths.get(outFile));

        assertTrue(fileStore.getTotalSpace() > 0);

    }
}
