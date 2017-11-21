package zoo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Wathcer（）观察接口
 * process是一个毁掉方法，当指定的监听事件发生时，会触发
 * Event.KeeperState.SyncConnected代表的是客户端链接服务器的状态变化
 *
 * 这个链接是一个非阻塞链接， 所以用闭锁实现
 * Created by gao on 2016/11/30.
 */
public class ZooConnectTest {
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        final CountDownLatch latch=new CountDownLatch(1);
        ZooKeeper zk=new ZooKeeper("192.168.43.114:2181", 3000, new Watcher() {
            public void process(WatchedEvent event) {
                if(event.getState()== Event.KeeperState.SyncConnected){
                    System.out.println("连接成功");
                    latch.countDown();
                }
            }
        });
        //确保上面已经链接
        latch.await();
        /*
            path 指定创建的路径
            data 初始数据，字节数组烈性
            acl 节点的访问权限，Ids.OPEN_ACL_UNSAFE代表：
            0000 0001
            0000 0010
            0000 0100
            0000 1000
            0001 0000
            具有所有的权限
            createMode 节点类型
        */
        zk.create("/park05","helloword".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    }
    @Test
    public void getConnection2() throws IOException, InterruptedException, KeeperException {
        final CountDownLatch latch=new CountDownLatch(1);
        ZooKeeper zk=new ZooKeeper("192.168.43.114:2181", 3000, new Watcher() {
            public void process(WatchedEvent event) {
                if(event.getState()== Event.KeeperState.SyncConnected){
                    System.out.println("连接成功");
                    latch.countDown();
                }
            }
        });
        //确保上面已经链接
        latch.await();
        //节点信息，当调用zk。getData时自动封装到stat对象中
        Stat stat=new Stat();
       byte[] data= zk.getData("/park01", new Watcher() {
            public void process(WatchedEvent event) {
                //监听当前数据节点数据变化的事件
                if(event.getType()== Event.EventType.NodeDataChanged){
                    System.out.println("当前节点数据发生变化");
                }
            }
        },stat);
        //zk.getData("park01",null,null);
    }

    /**
     * 跟新数据
     * @throws IOException
     */
    public void testSet() throws IOException, InterruptedException, KeeperException {
        final CountDownLatch latch=new CountDownLatch(1);
        ZooKeeper zk=new ZooKeeper("192.168.43.114:2181", 3000, new Watcher() {
            public void process(WatchedEvent event) {
                if(event.getState()== Event.KeeperState.SyncConnected){
                    System.out.println("连接成功");
                    latch.countDown();
                }
            }
        });
        //确保上面已经链接
        latch.await();
        /*
        * version 数据版本号
        * 如果要实现无论如何都要更新，这个数字写-1
        * 每次跟新成功后，数据版本号要+1
        * */
        zk.setData("park01","123456".getBytes(),-1);
        while(true);


    }
    public  void test1() throws IOException, InterruptedException, KeeperException {
        final CountDownLatch latch=new CountDownLatch(1);
        ZooKeeper zk=new ZooKeeper("192.168.43.114:2181", 3000, new Watcher() {
            public void process(WatchedEvent event) {
                if(event.getState()== Event.KeeperState.SyncConnected){
                    System.out.println("连接成功");
                    latch.countDown();
                }
            }
        });
        //确保上面已经链接
        latch.await();
        List<String> data=zk.getChildren("park01",null);
        for(String path:data) System.out.println(data);
        while(true);
    }
    //删除
    /*
    * 先拿到子节点路径，然后循环删除子节点，最后删除当前节点*/
    public  void testDel() throws IOException, InterruptedException, KeeperException {
        final CountDownLatch latch=new CountDownLatch(1);
        ZooKeeper zk=new ZooKeeper("192.168.43.114:2181", 3000, new Watcher() {
            public void process(WatchedEvent event) {
                if(event.getState()== Event.KeeperState.SyncConnected){
                    System.out.println("连接成功");
                    latch.countDown();
                }
            }
        });
        //确保上面已经链接
        latch.await();

        zk.delete("park01",-1);
        while(true);
    }
    //永久监听
    //用闭锁
    public void foreverListener() throws IOException, InterruptedException, KeeperException {
        final CountDownLatch latch=new CountDownLatch(1);
        ZooKeeper zk=new ZooKeeper("192.168.43.114:2181", 3000, new Watcher() {
            public void process(WatchedEvent event) {
                if(event.getState()== Event.KeeperState.SyncConnected){
                    System.out.println("连接成功");
                    latch.countDown();
                }
            }
        });
        //确保上面已经链接
        latch.await();

        zk.getData("/park01", new Watcher() {
            public void process(WatchedEvent event) {
                if(event.getType()== Event.EventType.NodeDataChanged){
                    System.out.println("数据发生变化");
                }
            }
        },null);
        System.out.println("1");
    }
}
