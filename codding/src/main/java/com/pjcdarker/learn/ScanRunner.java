package com.pjcdarker.learn;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pjcdarker
 * @created 10/21/2017.
 */
public class ScanRunner {

    /**
     * @param packageName
     *
     * @return
     */
    public static List<Class<?>> findClassScanPackage(String packageName) {
        String pathName = packageName.replace(".", "/");
        URL url = ScanRunner.class.getClassLoader()
                                  .getResource(pathName);
        if (url == null) {
            throw new InvalidPathException("'" + packageName + "' cannot be found by the ClassLoader", packageName);
        }

        List<Class<?>> classes = new ArrayList<>();
        try {
            Path path = Paths.get(url.toURI());
            if (!Files.exists(path) || !Files.isDirectory(path)) {
                throw new InvalidPathException("'" + packageName + "' is not a valid path", packageName);
            }
            Files.walk(path, 1)
                 .filter(p -> !Files.isDirectory(p))
                 .forEach(p -> {
                     String fileName = p.getFileName()
                                        .toString();
                     String className = fileName.substring(0, fileName.length() - 6);
                     try {
                         Class<?> clazz = Class.forName(packageName + "." + className);
                         classes.add(clazz);
                     } catch (ClassNotFoundException e) {
                         e.printStackTrace();
                         System.out.println("Unable to resolve class for '" + packageName + "." + className + "'");
                     }
                 });

        } catch (URISyntaxException ex) {
            throw new InvalidPathException("'" + packageName + "' is not a valid path", ex.getReason());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }
}
