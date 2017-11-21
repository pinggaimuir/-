package bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by gao on 2016/11/28.
 */
public class SelectorClient {
    public static void main(String[] args) throws IOException {
        SocketChannel sc=SocketChannel.open();
        sc.connect(new InetSocketAddress("127.0.0.1",8888));
        ByteBuffer buffer=ByteBuffer.wrap("nihao".getBytes());
        sc.write(buffer);
        ByteBuffer data=ByteBuffer.allocate(5);
        while(data.hasRemaining()){
            sc.read(data);
        }
        System.out.println(new String(data.array()));
        sc.close();
        while(true);

    }
}
