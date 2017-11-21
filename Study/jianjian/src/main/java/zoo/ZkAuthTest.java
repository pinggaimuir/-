package zoo;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by gao on 2016/12/4.
 */
public class ZkAuthTest {
    final static String PATH="/zk-test";

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        ZooKeeper zk=new ZooKeeper("192.168.8.160:2181",3000,null);
        zk.addAuthInfo("digest","foo:true".getBytes());
        zk.create(PATH,"123".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);
        ZooKeeper zk2=new ZooKeeper("192.168.8.160:2181",3000,null);
        byte[] b=zk.getData(PATH,false,null);
        System.out.println(new String(b));
        Thread.sleep(Integer.MAX_VALUE);
    }
    @Test
    public void testGetData() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zk=new ZooKeeper("192.168.8.160:2181",3000,null);
        zk.addAuthInfo("digest","foo:true".getBytes());
        byte[] b=zk.getData(PATH,false,null);
        System.out.println(b);
    }
}
