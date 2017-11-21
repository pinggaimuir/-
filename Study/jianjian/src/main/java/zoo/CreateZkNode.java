package zoo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.protocol.ClientProtocol;
import org.apache.hadoop.hdfs.server.namenode.LeaseManager;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

/**
 * //创建节点
 * Created by gao on 2016/12/4.
 */
public class CreateZkNode implements Watcher {
    private static CountDownLatch latch=new CountDownLatch(1);


    public void process(WatchedEvent watchedEvent) {
        Configuration conf=new Configuration();
        //hdfs同时只能有一个写入操作，
        LeaseManager lease;
        DFSClient client;
        //客户端与namenode通信
        ClientProtocol protocol;
    }
}
