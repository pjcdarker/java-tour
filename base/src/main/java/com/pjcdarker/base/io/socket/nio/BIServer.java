package com.pjcdarker.base.io.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author pjcdarker
 */
public class BIServer {

    private static AtomicInteger counter = new AtomicInteger(0);

    public static void start(int port) throws IOException {

        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("port range: 1 ~ 65535");
        }

        try (ServerSocketChannel socketChannel = ServerSocketChannel.open()) {
            socketChannel.configureBlocking(false);
            socketChannel.bind(new InetSocketAddress(port), 1024);

            // open selector
            Selector selector = Selector.open();

            // register socketChannel to selector, listen accept
            socketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.err.println("BIServer start...");

            while (true) {
                selector.select();

                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    // handle accept
                    if (key.isAcceptable()) {
                        channelAccept(selector, key);
                        continue;
                    }

                    // handle read
                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        channelRead(channel);
                        continue;
                    }
                }
            }
        }
    }

    private static void channelAccept(Selector selector, SelectionKey key) throws IOException {
        System.err.println("accept conn: " + counter.incrementAndGet());
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverSocketChannel.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
    }

    private static void channelRead(SocketChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);
        buffer.flip();

        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        System.out.println(Thread.currentThread().getName() + " receives msg: " + new String(bytes, StandardCharsets.UTF_8));

        sendResponse(channel, buffer);
    }

    private static void sendResponse(SocketChannel channel, ByteBuffer buffer) throws IOException {
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        String content = new String(bytes, StandardCharsets.UTF_8);

        if ("exit".equals(content)) {
            channel.close();
            return;
        }

        content = content.replace("吗", "")
                         .replaceAll("(\\?)+(\\s)*", "!");

        ByteBuffer buf = ByteBuffer.wrap(content.getBytes(StandardCharsets.UTF_8));
        channel.write(buf);
    }

    public static void main(String[] args) throws IOException {
        BIServer.start(9000);
    }
}
