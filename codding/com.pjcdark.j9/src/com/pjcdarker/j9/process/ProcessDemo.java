package com.pjcdarker.j9.process;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author pjcdarker
 * @created 9/23/2017.
 * @see https://docs.oracle.com/javase/9/core/process-api1.htm#JSCOR-GUID-472F9FE1-DA6E-4A72-A19F-6EDF059B37B7
 */
public class ProcessDemo {


    public static void currentInfo() {
        ProcessHandle processHandle = ProcessHandle.current();
        ProcessHandle.Info info = processHandle.info();
        System.out.println(info);
    }


    public static void start() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("echo", "Hello World!");
        Process p = pb.start();

        System.out.println(p.pid());
        System.out.println(p.exitValue());

        p.destroy();
    }

    public static void setEnvTest() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("/bin/sh", "-c", "echo $horse $dog $HOME").inheritIO();
        pb.environment().put("horse", "oats");
        pb.environment().put("dog", "treats");
        pb.start().waitFor();
    }


    public static void getInfoTest() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("echo", "Hello World!");
        String na = "<not available>";
        Process p = pb.start();
        ProcessHandle.Info info = p.info();
        System.out.printf("Process ID: %s%n", p.pid());
        System.out.printf("Command name: %s%n", info.command().orElse(na));
        System.out.printf("Command line: %s%n", info.commandLine().orElse(na));

        System.out.printf("Start time: %s%n",
                          info.startInstant().map(i -> i.atZone(ZoneId.systemDefault())
                                                        .toLocalDateTime().toString())
                              .orElse(na));

        System.out.printf("Arguments: %s%n",
                          info.arguments().map(a -> Stream.of(a)
                                                          .collect(Collectors.joining(" ")))
                              .orElse(na));

        System.out.printf("User: %s%n", info.user().orElse(na));
    }


    public static void redirectToFileTest() throws IOException, InterruptedException {
        File outFile = new File("out.tmp");
        Process p = new ProcessBuilder("ls", "-la")
                .redirectOutput(outFile)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start();
        int status = p.waitFor();
        if (status == 0) {
            p = new ProcessBuilder("cat", outFile.toString())
                    .inheritIO()
                    .start();
            p.waitFor();
        }
    }
}
