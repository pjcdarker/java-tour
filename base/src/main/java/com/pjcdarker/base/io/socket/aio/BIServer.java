package com.pjcdarker.base.io.socket.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

/**
 * @author pjcdarker
 */
public class BIServer {

    public static void start(int port) throws IOException {

        try (AsynchronousServerSocketChannel socketChannel = AsynchronousServerSocketChannel.open()) {
            socketChannel.bind(new InetSocketAddress(port), 1024);

            socketChannel.accept(socketChannel, new AcceptHandler());
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
            channel.write(buf, buf, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    if (attachment.hasRemaining()) {
                        channel.write(attachment);
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {

                }
            });
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {

        }
    }

    public static void main(String[] args) throws IOException {
        BIServer.start(9000);
    }

}
