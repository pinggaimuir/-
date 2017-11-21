package bio;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * nio编程，非阻塞是编程需要人为烤炉很多因素，
 * Created by gao on 2016/11/28.
 */
public class ChannelTest {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc=ServerSocketChannel.open();
        try {
            ssc.socket().bind(new InetSocketAddress(8887));

            SocketChannel socketChannel = null;
            while(socketChannel==null){
                socketChannel=ssc.accept();
            }
             while(!socketChannel.isConnected()){
                 socketChannel.finishConnect();
             }
            //默认为阻塞策略，改成false后，为非阻塞方式
            /*
                注意：
                    非阻塞的编程思想，是否有客户端连接需要人为的去判断
            * */
            ssc.configureBlocking(false);
            ByteBuffer buffer = ByteBuffer.allocate(100);
            //确保数据读取完整
            while(buffer.hasRemaining()){
                socketChannel.read(buffer);
            }
            System.out.println(new String(buffer.array()));
        }finally {
            ssc.close();
        }
    }
}
