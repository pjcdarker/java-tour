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


public class ClassScanner {

    public List<Class<?>> scanPackage(String packageName) {
        String pathName = packageName.replace(".", "/");
        URL url = ClassScanner.class.getClassLoader().getResource(pathName);

        if (url == null) {
            throw new InvalidPathException(packageName + " is not found by the ClassLoader", packageName);
        }

        List<Class<?>> classes = new ArrayList<>();
        try {
            Path path = Paths.get(url.toURI());
            if (!Files.exists(path) || !Files.isDirectory(path)) {
                throw new InvalidPathException(packageName + " is error package.", packageName);
            }

            try (Stream<Path> stream = Files.walk(path)) {
                stream.filter(p -> p.toFile().isFile())
                      .forEach(p -> {
                          try {
                              classes.add(forClass(packageName, p));
                          } catch (ClassNotFoundException e) {
                              e.printStackTrace();
                          }
                      });
            }
        } catch (URISyntaxException ex) {
            throw new InvalidPathException(packageName + " is not a valid path", ex.getReason());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    private Class<?> forClass(String packageName, Path path) throws ClassNotFoundException {
        String fileName = path.getFileName().toString();
        int suffixLength = ".class".length();

        String className = fileName.substring(0, fileName.length() - suffixLength);
        String classFullName = packageName + "." + className;

        return Class.forName(classFullName);
    }

}
