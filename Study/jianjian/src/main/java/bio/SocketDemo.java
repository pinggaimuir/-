package bio;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by gao on 2016/11/28.
 */
public class SocketDemo {
    @Test
    public void accept() throws IOException {
        ServerSocket ss=new ServerSocket();
        ss.bind(new InetSocketAddress(8888));
        Socket socket=ss.accept();
        System.out.println("zusefangkai");
        InputStream in=socket.getInputStream();
        in.read();
    }
    @Test
    public void connect() throws IOException {
        Socket s=new Socket();
        s.connect(new InetSocketAddress("127.0.0.1",8888));
        OutputStream out=s.getOutputStream();
        System.out.println("介入客户端");
        for (int i = 0; i < 100000; i++) {
            out.write("jieru".getBytes());
            System.out.println(i);
        }
        s.close();
    }
}
