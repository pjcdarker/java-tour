package com.pjcdarker.base.io.socket.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @author pjcdarker
 */
public class BIClient {

    public static void connect(String ip, int port) throws IOException {

        try (SocketChannel sc = SocketChannel.open();
            Selector selector = Selector.open()) {

            sc.configureBlocking(false);

            int ops = SelectionKey.OP_CONNECT;
            if (sc.connect(new InetSocketAddress(ip, port))) {
                // if finish connect
                ops = SelectionKey.OP_WRITE;
            }
            sc.register(selector, ops);

            System.err.println("BIClient start ....");

            while (true) {
                selector.select();

                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    SocketChannel channel = (SocketChannel) key.channel();
                    if (key.isConnectable()) {
                        channelConnect(selector, ops, channel);
                        continue;
                    }

                    if (key.isWritable()) {
                        channelWrite(channel);
                        channel.register(selector, SelectionKey.OP_READ);
                        continue;
                    }

                    if (key.isReadable()) {
                        channelRead(channel);
                        channel.register(selector, SelectionKey.OP_WRITE);
                        continue;
                    }
                }
            }
        }
    }

    private static void channelConnect(Selector selector, int ops, SocketChannel channel) throws IOException {
        if (channel.finishConnect()) {
            if (ops == SelectionKey.OP_CONNECT) {
                channel.register(selector, SelectionKey.OP_WRITE);
            }
        } else {
            System.err.println("链接失败");
            System.exit(1);
        }
    }

    private static void channelWrite(SocketChannel channel) throws IOException {

        System.err.print("wait input: ");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8.displayName()));

        String data = reader.readLine();
        System.err.println("input data: " + data);

        ByteBuffer buffer = ByteBuffer.wrap(data.getBytes(StandardCharsets.UTF_8.displayName()));
        channel.write(buffer);

        if ("q".equals(data)) {
            channel.close();
            System.exit(-1);
            return;
        }
    }

    private static void channelRead(SocketChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int read = channel.read(buffer);
        System.out.println("read: " + read);

        if (read <= 0) {
            return;
        }

        buffer.flip();

        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        System.out.println("BIClient receives msg: " + new String(bytes, StandardCharsets.UTF_8));
    }

    public static void main(String[] args) throws IOException {
        BIClient.connect("127.0.0.1", 9000);
    }
}
