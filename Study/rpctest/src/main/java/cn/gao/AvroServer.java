package cn.gao;

import avro.protocol.AddService;
import avro.protocol.AddServiceImpl;
import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.specific.SpecificResponder;

import java.net.InetSocketAddress;

/**
 * Created by gao on 2016/12/2.
 */
public class AvroServer {
    public static void main(String[] args) {
        NettyServer server=new NettyServer(new SpecificResponder(AddService.class,new AddServiceImpl()),
                new InetSocketAddress(8888));
        while(true);
    }
}
