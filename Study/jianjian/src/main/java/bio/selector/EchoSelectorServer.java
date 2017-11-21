package bio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

/**
 * Created by gao on 2016/11/28.
 */
public class EchoSelectorServer {
    private static int BUFSIZE=256;
    private static int TIMEOUT=3000;
    private static int PORT=8888;

    public static void main(String[] args) throws Exception {

        ServerSocketChannel channel=ServerSocketChannel.open();
        channel.socket().bind(new InetSocketAddress("127.0.0.1",8888));
        //只有非阻塞式的信道才能注册selector
        channel.configureBlocking(false);

        Selector selector=Selector.open();
        //将selector注册给channel，并赋予accept操作
        channel.register(selector, SelectionKey.OP_ACCEPT);

        Protocol protocol=new EchoProtocol(BUFSIZE);
        while(true){
            if(selector.select(TIMEOUT)==0){
                System.out.println("==");
                continue;
            }

            Iterator<SelectionKey> it= selector.selectedKeys().iterator();
            while(it.hasNext()){
                SelectionKey key=it.next();
                if(key.isAcceptable()){
                    protocol.HandleAccept(key);
                }
                if(key.isReadable()){
                    protocol.HandleRead(key);
                }
                if(key.isWritable()&&key.isValid()){
                    protocol.HandleWrite(key);
                }
                /*由于select（）操作只是向Selector所关联的键集合中添加元素
                * 因此如果不移除每个操作过的键
                * 它就会在下次调用select（）方法时扔保留在集合中
                * 而且可能会有无用的操作来调用它*/
                it.remove();
            }
        }
    }
}
