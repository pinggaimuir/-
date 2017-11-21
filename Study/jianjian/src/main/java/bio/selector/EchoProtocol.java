package bio.selector;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by gao on 2016/11/28.
 */
public class EchoProtocol implements Protocol {

    private  int bufsize;//为每个客户端通道创建的缓冲区大小

    public EchoProtocol(int bufsize){
        this.bufsize=bufsize;
    }
    public void HandleAccept(SelectionKey key) throws Exception {
        //channel()返回注册时用来创建的Channel，该channel是一个ServerSocketChannel,accept()方法返回一个SocketChannel
        SocketChannel channel=((ServerSocketChannel)key.channel()).accept();
        //只有非阻塞式信道才能注册Selector
        channel.configureBlocking(false);
        //通过selector()方法得到一个selector，根据指定的大小创建一个buffer，他将作为附件与返回的SelectionKey相关联
        channel.register(key.selector(),SelectionKey.OP_READ, ByteBuffer.allocateDirect(bufsize));
    }

    public void HandleRead(SelectionKey key) throws Exception {
        SocketChannel channel= (SocketChannel) key.channel();
        /*建立连接后有一个ByteBuffer附件加到该SelectionKey的实例上，这个附件将会在发送数据的时
         用到，附件始终是附着在这个长链接上的*/
        ByteBuffer buffer= (ByteBuffer) key.attachment();
        long byteRead=channel.read(buffer);
        /*如果read返回的是-1，说明底层链接已经关闭，这是需要关闭信道
        *关闭信道时将从选择器的各种集合中移除与该信道相关的键*/
        if(byteRead==-1){
            channel.close();
        }else{
            //仍然保留信道的可写操作，虽然缓冲区可能已经没有剩余的空间了，因为下次还要接受新的数据
            key.interestOps(SelectionKey.OP_READ|SelectionKey.OP_WRITE);
        }
    }

    public void HandleWrite(SelectionKey key) throws Exception {
        //附加到Selectkey上的buffer包含之前从信道中读取到的数据
        ByteBuffer buffer= (ByteBuffer) key.attachment();
        //修改缓冲区的状态，limit=position，position=0，
        buffer.flip();
        //得到信道
        SocketChannel channel= (SocketChannel) key.channel();
        //向信道中写入数据
        channel.write(buffer);
        if(!buffer.hasRemaining()){
            //如果没有剩余的数据可读，则修改键关联的操作集，指示只能进行读操作
            key.interestOps(SelectionKey.OP_READ);
        }
        //如果缓冲区有剩余的数据，该操作将剩余数据移到缓冲区的前端，使下次迭代能读入更多的数据
        buffer.compact();
    }
}
