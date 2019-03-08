package com.pjcdarker.base.io.socket.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

/**
 * @author pjcdarker
 */
public class BIServer {

    public static void start(int port) throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(1);
        try (AsynchronousServerSocketChannel socketChannel = AsynchronousServerSocketChannel.open()) {
            socketChannel.bind(new InetSocketAddress(port), 1024);
            socketChannel.accept(socketChannel, new AcceptHandler());

            downLatch.await();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

        @Override
        public void completed(AsynchronousSocketChannel result, AsynchronousServerSocketChannel attachment) {
            // accept next connect
            attachment.accept(attachment, this);
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            result.read(buffer, buffer, new ReadHandler(result));
        }

        @Override
        public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
            exc.printStackTrace();
        }
    }

    static class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {

        private AsynchronousSocketChannel channel;

        private ReadHandler(AsynchronousSocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public void completed(Integer result, ByteBuffer attachment) {
            attachment.flip();
            byte[] bytes = new byte[attachment.remaining()];
            attachment.get(bytes);

            String content = new String(bytes, StandardCharsets.UTF_8);
            System.out.println("msg: " + content);

            content = content.replace("吗", "")
                             .replaceAll("(\\?)+(\\s)*", "!");

            ByteBuffer buf = ByteBuffer.wrap(content.getBytes(StandardCharsets.UTF_8));
            channel.write(buf, buf, new WriteHandler(channel));
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
            exc.printStackTrace();
        }
    }

    static class WriteHandler implements CompletionHandler<Integer, ByteBuffer> {

        private AsynchronousSocketChannel channel;

        private WriteHandler(AsynchronousSocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public void completed(Integer result, ByteBuffer attachment) {
            if (attachment.hasRemaining()) {
                channel.write(attachment);
            }
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
            exc.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BIServer.start(9000);
    }

}
