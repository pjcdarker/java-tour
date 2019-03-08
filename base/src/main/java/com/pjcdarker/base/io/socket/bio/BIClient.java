package com.pjcdarker.base.io.socket.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author pjcdarker
 */
public class BIClient {

    private static void connect(String ip, int port) throws IOException {
        while (true) {
            System.err.print("wait input: ");
            Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.displayName());

            String data = scanner.nextLine();
            System.err.println("input data: " + data);
            if ("exit".equals(data)) {
                System.exit(-1);
                break;
            }

            // open a new channel each time
            try (SocketChannel channel = SocketChannel.open()) {
                channel.connect(new InetSocketAddress(ip, port));
                System.err.println("BIClient start ....");

                handleRequest(channel, data);
                handleResponse(channel);
            }
        }
    }

    private static void handleRequest(SocketChannel channel, String data) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(data.getBytes(StandardCharsets.UTF_8.displayName()));
        channel.write(buffer);
    }

    private static void handleResponse(SocketChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);
        buffer.flip();

        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        System.out.println("BIClient receives msg: " + new String(bytes, StandardCharsets.UTF_8));
    }

    public static void main(String[] args) throws IOException {
        BIClient.connect("127.0.0.1", 9000);
    }
}
