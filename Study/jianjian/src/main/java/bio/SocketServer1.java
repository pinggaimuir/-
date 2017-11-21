package bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;

/**
 * Created by gao on 2016/11/28.
 */
public class SocketServer1 {
    public static void main(String[] args) throws IOException {
        ServerSocket ss=new ServerSocket();
        ss.bind(new InetSocketAddress(8888));
        while(true){
            Socket socket=ss.accept();
            new Thread(new SocketRunner(socket));
        }
    }
}
class SocketRunner implements Runnable{
    private Socket socket;
    public SocketRunner(Socket socket){
        this.socket=socket;
    }
    public void run() {
        try {
            InputStream in=socket.getInputStream();
            System.out.println(in.read());
            System.out.println("当前线程为："+Thread.currentThread().getId());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
