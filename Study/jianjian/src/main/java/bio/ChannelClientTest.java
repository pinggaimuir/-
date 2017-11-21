package bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by gao on 2016/11/28.
 */
public class ChannelClientTest {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        try {
            sc.configureBlocking(false);
            sc.connect(new InetSocketAddress("127.0.0.1", 8887));
            System.out.println("客户端阻塞放开");
            ByteBuffer buffer = ByteBuffer.wrap("hello gao".getBytes());
            while(buffer.hasRemaining()) {
                sc.write(buffer);
            }
        }finally {
            sc.close();
        }
        while (true);
    }
}
