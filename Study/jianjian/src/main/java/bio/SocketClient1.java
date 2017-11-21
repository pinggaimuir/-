package bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 时间归纳为4种：
 *         1.accept 服务端接受客户端连入事件
 *         2.connect 呵护短 连入服务端时间
 *         3。read 读时间
 *         4.write 写时间
 *    引入selector ：多路复用选择器，基于事件监听，来控制线程状态变化，减少cpu负荷，提高线程的复用率
 *                  使得大量的访问段请求的场景得以实现，
 *                  设计模式：反应堆设计模式
 *      机制1：惊群：吧所有的阻塞线程都唤醒，  隐患 在短时间内，cpu的复合骤升，严重会导致卡顿或死机
 *    linux中有个epoll中也有类似的模式
 * Created by gao on 2016/11/28.
 */
public class SocketClient1 {
    public static void main(String[] args) throws IOException {
        Socket socket=new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1",8888));
        OutputStream out=socket.getOutputStream();
        out.write("gao".getBytes());
        out.close();
    }
}
