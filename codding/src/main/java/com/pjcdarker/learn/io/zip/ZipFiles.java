package com.pjcdarker.learn.io.zip;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author pjcdarker
 * @created 10/22/2017.
 */
public class ZipFiles {

    public static boolean unzip(String zipFile, String targetPath) {
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            byte[] buffer = new byte[1024];
            while (zipEntry != null) {
                String filename = zipEntry.getName();

                Path path = Paths.get(targetPath);
                if (!Files.exists(path)) {
                    Files.createDirectory(path);
                }
                Path filePath = Paths.get(targetPath + File.separator + filename);
                if (!Files.exists(filePath)) {
                    filePath = Files.createFile(filePath);
                }

                OutputStream out = Files.newOutputStream(filePath);

                int len;
                while ((len = zipInputStream.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
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


    /**
     * @param sourcesFile
     * @param compressFile
     */
    public static void compress(final String sourcesFile, final String compressFile) {
        try (final FileOutputStream fos = new FileOutputStream(compressFile);
             final ZipOutputStream zipOutputStream = new ZipOutputStream(fos)) {
            final File fileToZip = new File(sourcesFile);
            compressFile(fileToZip, fileToZip.getName(), zipOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void compressFile(final File fileToZip, final String filename, final ZipOutputStream zipOut) {
        if (fileToZip.isHidden()) {
            return;
        }
        Path path = Paths.get(fileToZip.getPath());
        if (Files.isDirectory(path)) {
            try {
                Files.list(path).forEach(p -> compressFile(p.toFile(), filename + File.separator + p.getFileName(), zipOut));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try (final FileInputStream fis = new FileInputStream(fileToZip)) {
            final ZipEntry zipEntry = new ZipEntry(filename);
            zipOut.putNextEntry(zipEntry);
            final byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
