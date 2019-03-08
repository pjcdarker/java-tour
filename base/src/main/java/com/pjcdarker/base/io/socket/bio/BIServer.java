package com.pjcdarker.base.io.socket.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author pjcdarker
 */
public class BIServer {

    public static void start(int port) throws IOException {
        try (ServerSocketChannel socketChannel = ServerSocketChannel.open()) {
            socketChannel.bind(new InetSocketAddress(port), 1024);

            System.err.println("BIServer start ....");

            while (true) {
                SocketChannel channel = socketChannel.accept();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                channel.read(buffer);
                byte[] data = channelRead(buffer);
                channelWrite(channel, data);
            }
        }
    }

    public static void startPool(int port) throws IOException {
        try (ServerSocketChannel socketChannel = ServerSocketChannel.open()) {
            socketChannel.bind(new InetSocketAddress(port), 1024);

            System.err.println("BIServer start ....");
            final ThreadPoolExecutor executor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(), 128,
                2, TimeUnit.MINUTES, new LinkedBlockingDeque<>());

            while (true) {
                SocketChannel channel = socketChannel.accept();

                executor.execute(() -> {
                    try {
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        channel.read(buffer);
                        byte[] data = channelRead(buffer);
                        channelWrite(channel, data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    private static byte[] channelRead(ByteBuffer buffer) {
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        System.out.println(Thread.currentThread().getName() + " receives msg: " + new String(bytes, StandardCharsets.UTF_8));

        return bytes;
    }

    private static void channelWrite(SocketChannel channel, byte[] bytes) throws IOException {
        String content = new String(bytes, StandardCharsets.UTF_8);
        content = content.replace("吗", "")
                         .replaceAll("\\?(\\s)*", "!");

        ByteBuffer buffer = ByteBuffer.wrap(content.getBytes(StandardCharsets.UTF_8));
        channel.write(buffer);
        channel.close();
    }

    public static void main(String[] args) throws IOException {
        BIServer.startPool(9000);
    }
}
