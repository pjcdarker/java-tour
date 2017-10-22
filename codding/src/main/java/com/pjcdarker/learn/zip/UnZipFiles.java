package com.pjcdarker.learn.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author pjcdarker
 * @created 10/21/2017.
 */
public class UnZipFiles {

    public static boolean zip(String zipFile, String targetPath) {

        byte[] buffer = new byte[1024];
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                String filename = zipEntry.getName();

                Path path = Paths.get(targetPath);
                if (!Files.exists(path)) {
                    Files.createDirectory(path);
                }
                Path pathFile = Files.createFile(Paths.get(targetPath + File.separator + filename));
                while (zipInputStream.read(buffer) > 0) {
                    Files.write(pathFile, buffer);
                }
                zipEntry = zipInputStream.getNextEntry();
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
