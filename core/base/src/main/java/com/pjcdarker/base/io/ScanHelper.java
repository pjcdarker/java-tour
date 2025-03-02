package com.pjcdarker.base.io;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author pjcdarker
 */
public class ScanHelper {

    /**
     * find class by package.
     */
    public static List<Class<?>> findClassScanPackage(String packageName) {
        String pathName = packageName.replace(".", "/");
        URL url = ScanHelper.class.getClassLoader().getResource(pathName);

        if (url == null) {
            throw new InvalidPathException("'" + packageName + "' not found by the ClassLoader", packageName);
        }

        List<Class<?>> classes = new ArrayList<>();
        try {
            Path path = Paths.get(url.toURI());
            if (!path.toFile().exists() || !path.toFile().isDirectory()) {
                throw new InvalidPathException("'" + packageName + "' is not a valid path", packageName);
            }

            try (Stream<Path> stream = Files.walk(path, 1)) {
                stream.filter(p -> p.toFile().isFile())
                      .forEach(p -> {
                          String fileName = p.getFileName().toString();
                          String className = fileName.substring(0, fileName.length() - 6);
                          try {
                              Class<?> clazz = Class.forName(packageName + "." + className);
                              classes.add(clazz);
                          } catch (ClassNotFoundException e) {
                              e.printStackTrace();
                              System.out.println("Unable to resolve class for '" + packageName + "." + className + "'");
                          }
                      });
            }
        } catch (URISyntaxException ex) {
            throw new InvalidPathException("'" + packageName + "' is not a valid path", ex.getReason());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }
}
