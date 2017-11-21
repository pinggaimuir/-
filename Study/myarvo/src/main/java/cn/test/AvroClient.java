package cn.test;

import avro.protocol.AddService;
import cn.gao.User;
import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by gao on 2016/12/2.
 */
public class AvroClient {
    public static void main(String[] args) throws IOException {
        NettyTransceiver client=new NettyTransceiver(new InetSocketAddress("127.0.0.1",8888));
        AddService protocol= SpecificRequestor.getClient(AddService.class,client);
        int result=protocol.add(2,3);
        System.out.println(result);
        protocol.sendUser(new User("gao","feng",22));
    }
}
