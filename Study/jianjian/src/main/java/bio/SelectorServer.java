package bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by gao on 2016/11/28.
 */
public class SelectorServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc= ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.socket().bind(new InetSocketAddress("127.0.0.1",8888));
        Selector selector=Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        while(true){
            //阻塞方法吗，知道有事件发生时，阻塞放开
            selector.select();
            //如果代码都早这里说明有事件产生，通过调用selectedKeys() 拿到当前监听的事件集合
            Set<SelectionKey> set=selector.selectedKeys();
            Iterator<SelectionKey> it=set.iterator();
            while(it.hasNext()){
                SelectionKey sk=it.next();
                if(sk.isAcceptable()){
                    //写接入代码
                    ServerSocketChannel ss= (ServerSocketChannel) sk.channel();
                    //服务端和客户端进行通信，都通过socketchannel来做
                    SocketChannel s=ss.accept();
                    //设置每一个用户的socketChannel是非阻塞的，目的是实现用少量的线程处理多用户请求
                    s.configureBlocking(false);
                    //在socketChannel上注册读或者写的事件
                    System.out.println("当前线程："+Thread.currentThread().getId());
                    s.register(selector,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
                }
                if(sk.isReadable()){
                    //处理读事件
                    SocketChannel ss= (SocketChannel) sk.channel();
                    ByteBuffer buffer= ByteBuffer.allocate(5);
                    /*  hasReMaining保证数据可以完全读取
                        如果创建的缓存区的大小和发送端发送的数据大小不一致，则跳不出循环
                        可以借鉴协议的思想，先拿到数据的实际大小，然后再创建相对应的缓存区大小
                    */
                    while(buffer.hasRemaining()){
                        ss.read(buffer);
                    }
                    System.out.println("客户端发送的数据为："+new String(buffer.array()));
                    System.out.println("当前线程："+Thread.currentThread().getId());
                    //去掉读事件
                    ss.register(selector,sk.interestOps()&~SelectionKey.OP_READ);
                }
                if(sk.isWritable()){
                    //处理写事件
                    SocketChannel sc= (SocketChannel) sk.channel();
                    ByteBuffer buffer=ByteBuffer.wrap("hello".getBytes());
                    while(buffer.hasRemaining()){
                        sc.write(buffer);
                    }
                    sc.register(selector,sk.interestOps()&~SelectionKey.OP_WRITE);
                }
                //删除该事件，防止重复处理
                it.remove();
            }
        }
    }
}
