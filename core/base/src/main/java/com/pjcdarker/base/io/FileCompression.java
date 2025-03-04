package com.pjcdarker.base.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileCompression {

    public void unzip(String zipFile, String distPath) {
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();

            if (zipEntry != null) {
                Path path = Paths.get(distPath);
                if (!Files.isDirectory(path)) {
                    Files.createDirectory(path);
                }
            }

            byte[] buffer = new byte[1024];
            while (zipEntry != null) {
                String filename = zipEntry.getName();
                Path filePath = Paths.get(distPath + File.separator + filename);
                try (OutputStream out = Files.newOutputStream(filePath)) {
                    int len;
                    while ((len = zipInputStream.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                }
                zipEntry = zipInputStream.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void zip(String inputFile, String outFile) {
        try (FileOutputStream fos = new FileOutputStream(outFile);
            ZipOutputStream zipOutputStream = new ZipOutputStream(fos)) {

            File srcFile = new File(inputFile);
            zip(srcFile, srcFile.getName(), zipOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void zip(File srcFile, String filename, ZipOutputStream zipOut) {
        if (srcFile.isHidden()) {
            return;
        }

        Path path = Paths.get(srcFile.getPath());
        if (Files.isDirectory(path)) {
            try (Stream<Path> stream = Files.list(path)) {
                stream.forEach(p -> zip(p.toFile(), filename + File.separator + p.getFileName(), zipOut));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try (FileInputStream fis = new FileInputStream(srcFile)) {
            ZipEntry zipEntry = new ZipEntry(filename);
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
