package com.pjcdarker.base.io.socket.aio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

/**
 * @author pjcdarker
 */
public class BIClient {

    private static CountDownLatch downLatch = new CountDownLatch(1);

    public static void connect(String ip, int port) throws IOException, InterruptedException {
        AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
        socketChannel.connect(new InetSocketAddress(ip, port), socketChannel, new ConnectHandler());
        downLatch.await();
    }

    static class ConnectHandler implements CompletionHandler<Void, AsynchronousSocketChannel> {

        @Override
        public void completed(final Void result, final AsynchronousSocketChannel attachment) {
            System.err.println("connect finish");
            sendRequest(attachment);
        }

        @Override
        public void failed(final Throwable exc, final AsynchronousSocketChannel attachment) {
            exc.printStackTrace();
        }
    }

    static class WriteHandler implements CompletionHandler<Integer, ByteBuffer> {
        private AsynchronousSocketChannel channel;

        private WriteHandler(AsynchronousSocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public void completed(final Integer result, final ByteBuffer attachment) {
            if (attachment.hasRemaining()) {
                channel.write(attachment, attachment, this);
            } else {

                if ("q".equals(new String(attachment.array(), StandardCharsets.UTF_8))) {
                    try {
                        channel.close();
                        downLatch.countDown();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                channel.read(readBuffer, readBuffer, new ReadHandler());
            }
        }

        @Override
        public void failed(final Throwable exc, final ByteBuffer attachment) {
            exc.printStackTrace();
        }
    }

    static class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {

        @Override
        public void completed(final Integer result, final ByteBuffer attachment) {
            attachment.flip();
            byte[] bytes = new byte[attachment.remaining()];
            attachment.get(bytes);

            attachment.clear();

            String data = new String(bytes, StandardCharsets.UTF_8);

            System.err.println("client rev msg: " + data);
        }

        @Override
        public void failed(final Throwable exc, final ByteBuffer attachment) {
            exc.printStackTrace();
        }
    }

    private static void sendRequest(AsynchronousSocketChannel channel) {
        while (true) {
            System.err.println("wait input: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                String data = reader.readLine();
                System.out.println("input: " + data);

                ByteBuffer buffer = ByteBuffer.wrap(data.getBytes(StandardCharsets.UTF_8));
                channel.write(buffer, buffer, new WriteHandler(channel));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        connect("127.0.0.1", 9000);
    }
}
